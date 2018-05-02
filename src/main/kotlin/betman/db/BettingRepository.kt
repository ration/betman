package betman.db

import betman.pojos.Bet

interface BettingRepository {

    fun get(gameId: Int, username: String): Bet

    fun bet(gameId: Int, bet: Bet, username: String)
}