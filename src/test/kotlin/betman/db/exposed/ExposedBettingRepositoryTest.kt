package betman.db.exposed

import betman.pojos.Bet
import betman.pojos.ScoreBet
import org.junit.Test

class ExposedBettingRepositoryTest : DbTest() {

    private val repository = ExposedBettingRepository()

    @Test
    fun bet() {
        val bet = Bet(scores = listOf(ScoreBet(1, 1, 2)))
        repository.bet(1, bet)
    }

    @Test
    fun invalidGameId() {

    }
}