package betman

import betman.db.BettingRepository
import betman.pojos.Bet
import betman.pojos.Odds
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(loader = AnnotationConfigContextLoader::class)
class BettingControllerTest {


    @Mock
    lateinit var bettingRepository: BettingRepository

    @InjectMocks
    lateinit var betting: BettingController


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }


    @Test
    fun sendScores() {
        val bets = listOf(Bet(1, 10, 10))
        betting.addBets(0, 1, bets)
    }

    @Test
    fun getScores() {
        val ans = betting.bets(1, 1)
        Assert.assertEquals(48, ans.size)
    }

    @Test
    fun odds() {
        val bet = Odds(1,1.0,1.0, 1.0)
        whenever(bettingRepository.odds(1)).thenReturn(listOf(bet))

        val ans = betting.odds(1)
        Assert.assertEquals(bet,ans[0])

    }
}