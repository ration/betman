package betman.db

import betman.pojos.Bet
import io.reactivex.Maybe

interface BettingRepository {

    fun get(gameId: String, username: String): Maybe<Bet>

    fun bet(gameId: String, bet: Bet, username: String)
}