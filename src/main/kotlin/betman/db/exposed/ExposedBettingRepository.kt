package betman.db.exposed

import betman.db.BettingRepository
import betman.db.UnknownGameException
import betman.db.UnknownMatchException
import betman.db.UnknownUserException
import betman.db.exposed.Bets.user
import betman.db.exposed.Matches.externalId
import betman.db.exposed.Matches.game
import betman.db.exposed.Users.name
import betman.pojos.Bet
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class ExposedBettingRepository : BettingRepository {

    override fun get(gameId: Int, username: String): Bet {
        return transaction {
            val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
            val gameDao = GameDao.findById(gameId) ?: throw UnknownGameException()
            val bets = BetDao.find { (user eq userDao.id) and (game eq gameDao.id) }
            Bet()
        }
    }

    override fun bet(gameId: Int, bet: Bet, username: String) {
        return transaction {
            val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
            val gameDao = GameDao.findById(gameId) ?: throw UnknownGameException()
            for (bet in bet.scores) {
                val matchDao = MatchDao.find { externalId eq bet.matchId }.firstOrNull()
                        ?: throw UnknownMatchException(String.format("Unkown match id: %s", bet.matchId))
                BetDao.new {
                    match = matchDao.id
                    user = userDao.id
                    home = bet.home
                    away = bet.away
                }
            }
        }
    }


}