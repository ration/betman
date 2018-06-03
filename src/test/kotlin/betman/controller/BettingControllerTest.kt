package betman.controller

import betman.db.BettingRepository
import betman.db.GameRepository
import betman.db.GroupRepository
import betman.pojos.Bet
import betman.pojos.Game
import betman.pojos.Group
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
    lateinit var groupRepository: GroupRepository


    @Mock
    lateinit var gameRepository: GameRepository

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
        whenever(bettingRepository.get(eq(game), eq("name"))).thenReturn(Maybe.just(Bet(groupKey = game)))
        val ans = betting.bets(game, principal).blockingGet()
        verify(bettingRepository, times(1)).get(eq(game), eq("name"))
        assertEquals(game, ans.groupKey)
    }


    @Test
    fun bets() {
        whenever(bettingRepository.get(eq(game), eq("name"))).thenReturn(Maybe.just(Bet(groupKey = game)))
        val ans = betting.bets(game, principal.name).blockingGet()
        verify(bettingRepository, times(1)).get(eq(game), eq("name"))
        assertEquals(game, ans.groupKey)
    }


    @Test
    fun guess() {
        val key = "key"
        whenever(groupRepository.get(eq(key), eq("name"))).thenReturn(Maybe.just(Group(name = game, description = "some", game = game)))
        whenever(gameRepository.get(eq(game))).thenReturn(Maybe.just(Game(name = game, description = "some")))
        whenever(bettingRepository.get(eq(key), eq("name"))).thenReturn(Maybe.just(Bet(groupKey = game)))


        val bet: Bet = betting.guess(key, Bet(groupKey = game), principal).blockingGet()
        verify(gameRepository, times(1)).get(eq(game))
        verify(bettingRepository, times(1)).bet(any(), any(), any())
        assertNotNull(bet)
    }

}