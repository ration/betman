package betman.db.exposed

import betman.db.BettingRepository
import betman.pojos.Bet
import betman.pojos.Odds
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class ExposedBettingRepository : BettingRepository {
    override fun bet(game: Int, bet: Bet) {
        return transaction {

        }
    }


    override fun odds(game: Int): List<Odds> {
        return listOf()
    }
}