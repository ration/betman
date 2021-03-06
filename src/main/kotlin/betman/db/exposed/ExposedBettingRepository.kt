package betman.db.exposed

import betman.RxUtils.maybeNull
import betman.UnknownGameException
import betman.UnknownGroupException
import betman.UnknownMatchException
import betman.UnknownUserException
import betman.db.BettingRepository
import betman.db.CacheRepository
import betman.db.exposed.Matches.externalId
import betman.db.exposed.Users.name
import betman.pojos.Bet
import betman.pojos.ScoreBet
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Maybes
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component
import java.time.Instant
import betman.db.exposed.Games.name as gameName

@Component
class ExposedBettingRepository : BettingRepository {

    private val matchDaoCache = CacheRepository.instance.getOrCreate<Int, MatchDao>("matchDaoCache") {
        Single.just(MatchDao.findById(it))
    }

    private val betCache = CacheRepository.instance.getOrCreate<Pair<String,String>, Bet>("bettingCache") {
        getFromDb(it.first,it.second).toSingle()
    }

    override fun get(groupId: String, username: String): Maybe<Bet> {
        return betCache.get(Pair(groupId, username))
    }

    fun getFromDb(groupId: String, username: String): Maybe<Bet> {
        return Maybes.maybeNull(transaction {
            val userDao = UserDao.find { name eq username }.singleOrNull() ?: throw UnknownUserException()
            val groupDao = GroupDao.find { Groups.key eq groupId }.singleOrNull() ?: throw UnknownGroupException()
            val gameDao = GameDao.findById(groupDao.game.value) ?: throw UnknownGameException()

            val bets = getMatchBets(userDao, gameDao, groupDao)
            val winner: TeamDao? = getWinnerBet(userDao, gameDao, groupDao)
            val goalking: String? = getGoalKingBet(userDao, gameDao, groupDao)
            val userDisplayName = GroupUserDao.find { (GroupUser.user eq userDao.id) and (GroupUser.group eq groupDao.id)}.map { it.name }.singleOrNull()

            Bet(groupKey = groupDao.key, scores = bets, winner = winner?.externalId, goalKing = goalking, userDisplayName = userDisplayName)
        })
    }

    private fun getGoalKingBet(userDao: UserDao, gameDao: GameDao, groupDao: GroupDao): String? {
        return BetDao.wrapRows(Bets.innerJoin(Matches).innerJoin(Games).select {
            (Bets.goalking.isNotNull()) and (Bets.user eq userDao.id) and (gameName eq gameDao.name) and (Bets.group eq groupDao.id)
        }).map { it.goalKing }.firstOrNull()
    }

    private fun getWinnerBet(userDao: UserDao, gameDao: GameDao, groupDao: GroupDao): TeamDao? {
        return BetDao.wrapRows(Bets.innerJoin(Matches).innerJoin(Games).select {
            (Bets.winner.isNotNull()) and (Bets.user eq userDao.id) and (gameName eq gameDao.name) and (Bets.group eq groupDao.id)
        }).map { it.winner }.mapNotNull { TeamDao.findById(it!!.value) }.firstOrNull()
    }

    private fun getMatchBets(userDao: UserDao, gameDao: GameDao, groupDao: GroupDao): List<ScoreBet> {
        return BetDao.wrapRows(Bets.innerJoin(Matches).innerJoin(Games).select {
            (Bets.match.isNotNull()) and (Bets.user eq userDao.id) and (gameName eq gameDao.name) and (Bets.group eq groupDao.id)
        }).map {
            val matchDao = matchDaoCache.get(it.match!!.value).blockingGet()
            ScoreBet(matchId = matchDao!!.externalId, home = it.home, away = it.away)
        }
    }

    override fun bet(groupId: String, bet: Bet, username: String): Single<Bet> {
        return Observable.just(transaction {
            val userDao = UserDao.find { name eq username }.singleOrNull() ?: throw UnknownUserException()
            val groupDao = GroupDao.find { Groups.key eq groupId }.singleOrNull() ?: throw UnknownGroupException()
            val teamDao: TeamDao? = if (bet.winner != null) TeamDao.find { Teams.externalId eq bet.winner!! }.firstOrNull() else null
            val gameDao = GameDao.findById(groupDao.game.value) ?: throw UnknownGameException()

            for (score in bet.scores) {
                val matchDao = MatchDao.find { (externalId eq score.matchId) and (Matches.game eq gameDao.id) }.singleOrNull()
                        ?: throw UnknownMatchException("Unknown match id: ${score.matchId}")

                val old = BetDao.wrapRows(Bets.innerJoin(Matches).innerJoin(Games).select {
                    (Bets.user eq userDao.id) and (gameName eq gameDao.name) and (Bets.group eq groupDao.id) and (Matches.externalId eq score.matchId)
                }).firstOrNull()
                // Simply ignore attemps to modify matches that have been played or started
                if (matchDao.date <= Instant.now().toEpochMilli()) {
                    if (old != null) {
                        score.home = old.home
                        score.away = old.away
                    } else {
                        // If the player is late, mark the bet as 0-0
                        BetDao.new {
                            match = matchDao.id
                            user = userDao.id
                            home = 0
                            away = 0
                            group = groupDao.id
                        }
                    }
                    continue
                }

                if (old != null) {
                    old.away = score.away
                    old.home = score.home
                    old.winner = teamDao?.id
                    old.goalKing = bet.goalKing
                } else {
                    BetDao.new {
                        match = matchDao.id
                        user = userDao.id
                        home = score.home
                        away = score.away
                        group = groupDao.id
                        winner = teamDao?.id
                        goalKing = bet.goalKing
                    }
                }
            }
            commit()
            CacheRepository.instance.invalidateAll()
            bet

        }).singleOrError()
    }


}