package betman

import betman.api.provider.GameDataProvider
import betman.db.GameRepository
import betman.pojos.Game
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DataLoaderTest {

    private val game = Game(id = 1, name = "name", description = "description")

    private val single: Maybe<Game> = Maybe.just(game)

    @Mock
    lateinit var provider: GameDataProvider

    @Mock
    lateinit var gameRepository: GameRepository


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        whenever(gameRepository.create(any())).thenReturn(Observable.just(game).singleOrError())
        whenever(provider.name).thenReturn("name")
        whenever(provider.description).thenReturn("description")
        whenever(provider.matches()).thenReturn(Observable.just(listOf()))
        whenever(provider.teams()).thenReturn(Observable.just(listOf()))
    }

    @Test
    fun loadNew() {
        whenever(gameRepository.get(any())).thenReturn(Maybe.empty())
        DataLoader().subscribe(provider, gameRepository)
        verify(gameRepository, times(1)).get(eq(game.name))
        verify(gameRepository, times(1)).create(any())
        verify(provider, times(1)).matches()
    }

    @Test
    fun loadExisting() {
        whenever(gameRepository.get(any())).thenReturn(single)
        DataLoader().subscribe(provider, gameRepository)
        verify(gameRepository, times(1)).get(eq(game.name))
        verify(gameRepository, times(0)).create(any())
        verify(gameRepository, times(1)).update(any())
    }
}