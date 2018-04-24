package betman.db.sql

import betman.db.BettingRepository
import betman.pojos.Odds
import org.springframework.stereotype.Component

@Component
class SqlBettingRepository: BettingRepository {
    override fun odds(game: Int): List<Odds> {
        return listOf()
    }
}