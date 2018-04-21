package betman

import betman.pojos.Bet
import org.junit.Test

class BettingControllerTest {
    @Test
    fun sendScores() {
        val betting = BettingController()
        val bets = listOf(Bet(1, 10,10))
        betting.addBets(0, bets)
    }
}