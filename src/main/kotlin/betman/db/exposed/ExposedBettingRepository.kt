package betman.db.exposed

import betman.db.BettingRepository
import betman.db.UnknownGameException
import betman.db.UnknownMatchException
import betman.db.UnknownUserException
import betman.db.exposed.Bets.game
import betman.db.exposed.Bets.user
import betman.db.exposed.Matches.externalId
import betman.db.exposed.Users.name
import betman.pojos.Bet
import betman.pojos.ScoreBet
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class ExposedBettingRepository : BettingRepository {

    override fun get(gameId: Int, username: String): Bet {
        return transaction {
            val userDao = UserDao.find { name eq username }.singleOrNull() ?: throw UnknownUserException()
            val gameDao = GameDao.findById(gameId) ?: throw UnknownGameException()
            val bets = BetDao.find { (user eq userDao.id) and (game eq gameDao.id) }.map {
                val matchDao = MatchDao.findById(it.match)
                ScoreBet(matchId = matchDao!!.externalId, home = it.home, away = it.away)
            }
            Bet(scores = bets)
        }
    }

    override fun bet(gameId: Int, bet: Bet, username: String) {
        return transaction {
            val userDao = UserDao.find { name eq username }.singleOrNull() ?: throw UnknownUserException()
            val gameDao = GameDao.findById(gameId) ?: throw UnknownGameException()
            for (score in bet.scores) {
                val matchDao = MatchDao.find { externalId eq score.matchId }.singleOrNull()
                        ?: throw UnknownMatchException(String.format("Unkown match id: %s", score.matchId))
                BetDao.new {
                    game = gameDao.id
                    match = matchDao.id
                    user = userDao.id
                    home = score.home
                    away = score.away
                }
            }
        }
    }


}