package betman.db.exposed

import betman.RxUtils.maybeNull
import betman.UnknownGameException
import betman.UnknownGroupException
import betman.UnknownMatchException
import betman.UnknownUserException
import betman.db.BettingRepository
import betman.db.exposed.Matches.externalId
import betman.db.exposed.Users.name
import betman.pojos.Bet
import betman.pojos.ScoreBet
import io.reactivex.Maybe
import io.reactivex.rxkotlin.Maybes
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component
import betman.db.exposed.Games.name as gameName

@Component
class ExposedBettingRepository : BettingRepository {

    override fun get(groupId: String, username: String): Maybe<Bet> {
        return Maybes.maybeNull(transaction {
            val userDao = UserDao.find { name eq username }.singleOrNull() ?: throw UnknownUserException()
            val groupDao = GroupDao.find { Groups.key eq groupId }.singleOrNull() ?: throw UnknownGroupException()
            val gameDao = GameDao.findById(groupDao.game.value) ?: throw UnknownGameException()

            val bets = BetDao.wrapRows(Bets.innerJoin(Matches).innerJoin(Games).select {
                (Bets.user eq userDao.id) and (gameName eq gameDao.name) and (Bets.group eq groupDao.id)
            }).map {
                val matchDao = MatchDao.findById(it.match)
                ScoreBet(matchId = matchDao!!.externalId, home = it.home, away = it.away)
            }
            Bet(groupId = groupDao.name, scores = bets)
        })
    }

    override fun bet(groupId: String, bet: Bet, username: String) {
        return transaction {
            val userDao = UserDao.find { name eq username }.singleOrNull() ?: throw UnknownUserException()
            for (score in bet.scores) {
                val matchDao = MatchDao.find { externalId eq score.matchId }.singleOrNull()
                        ?: throw UnknownMatchException(String.format("Unkown match id: %s", score.matchId))
                val groupDao = GroupDao.find { Groups.key eq groupId }.singleOrNull() ?: throw UnknownGroupException()
                val gameDao = GameDao.findById(groupDao.game.value) ?: throw UnknownGameException()
                val old = BetDao.wrapRows(Bets.innerJoin(Matches).innerJoin(Games).select {
                    (Bets.user eq userDao.id) and (gameName eq gameDao.name) and (Bets.group eq groupDao.id)
                }).singleOrNull()
                if (old != null) {
                    old.away = score.away
                    old.home = score.home
                } else {
                    BetDao.new {
                        match = matchDao.id
                        user = userDao.id
                        home = score.home
                        away = score.away
                        group = groupDao.id
                    }
                }
            }
        }
    }


}