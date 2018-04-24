package betman.db

import betman.pojos.Odds

interface BettingRepository {
    fun odds(game: Int): List<Odds>
}