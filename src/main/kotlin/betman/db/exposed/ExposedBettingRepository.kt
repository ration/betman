package betman.db.exposed

import betman.db.BettingRepository
import betman.pojos.Odds
import org.springframework.stereotype.Component

@Component
class ExposedBettingRepository : BettingRepository {
    override fun odds(game: Int): List<Odds> {
        return listOf()
    }
}