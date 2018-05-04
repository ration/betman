package betman.controller

import betman.db.BettingRepository
import betman.pojos.Bet
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import java.security.Principal

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(loader = AnnotationConfigContextLoader::class)
class BettingControllerTest {

    private val game = "game"

    @Mock
    lateinit var bettingRepository: BettingRepository

    @Mock
    lateinit var principal: Principal


    @InjectMocks
    lateinit var betting: BettingController


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        whenever(principal.name).thenReturn("name")
    }


    @Test
    fun addBets() {
        betting.addBets(game, Bet(game), principal)
        verify(bettingRepository, times(1)).bet(any(), any(), any())
    }

    @Test
    fun get() {
        whenever(bettingRepository.get(eq(game), eq("name"))).thenReturn(Maybe.just(Bet(gameId = game)))
        val ans = betting.bets(game, principal).blockingGet()
        verify(bettingRepository, times(1)).get(eq(game), eq("name"))
        assertEquals(game, ans.gameId)
    }


}