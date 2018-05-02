package betman.db

import betman.pojos.Bet
import betman.pojos.Odds

interface BettingRepository {
    fun odds(game: Int): List<Odds>

    fun bet(game: Int, bet: Bet)
}