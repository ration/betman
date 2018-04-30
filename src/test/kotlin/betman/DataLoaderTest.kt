package betman

import betman.api.provider.GameDataProvider
import betman.db.GameRepository
import betman.pojos.Game
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DataLoaderTest {

    private val game = Game(id = 1, name = "name", description = "description")

    @Mock
    lateinit var provider: GameDataProvider

    @Mock
    lateinit var gameRepository: GameRepository


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        whenever(gameRepository.create(any())).thenReturn(game)
        whenever(provider.name).thenReturn("name")
        whenever(provider.description).thenReturn("description")
    }

    @Test
    fun loadNew() {
        whenever(gameRepository.get(any())).thenReturn(null)
        DataLoader().load(provider, gameRepository)
        verify(gameRepository, times(1)).get(eq(game.name))
        verify(gameRepository, times(1)).create(any())
        verify(provider, times(1)).matches()
        verify(provider, times(1)).others()
    }

    @Test
    fun loadExisting() {
        whenever(gameRepository.get(any())).thenReturn(game)
        DataLoader().load(provider, gameRepository)
        verify(gameRepository, times(1)).get(eq(game.name))
        verify(gameRepository, times(0)).create(any())
        verify(gameRepository, times(1)).update(any())
    }
}