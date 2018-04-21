package betman

import betman.pojos.Bet
import org.junit.Assert
import org.junit.Test

class BettingControllerTest {
    private val betting = BettingController()

    @Test
    fun sendScores() {
        val bets = listOf(Bet(1, 10,10))
        betting.addBets(0, 1, bets)
    }

    @Test
    fun getScores() {
        val ans = betting.bets(1,1)
        Assert.assertEquals(48, ans.size)
    }
}