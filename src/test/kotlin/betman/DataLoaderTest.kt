package betman

import betman.db.GameRepository
import betman.gameprovider.fifa2018.Fifa2018Provider
import betman.pojos.Game
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DataLoaderTest {

    private val game = Game(id = 1, name = "name", description = "description")

    @Mock
    lateinit var provider: Fifa2018Provider

    @Mock
    lateinit var gameRepository: GameRepository

    private lateinit var loader: DataLoader

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        loader = DataLoader(provider, gameRepository)
        whenever(gameRepository.create(any())).thenReturn(game)
        whenever(provider.name).thenReturn("name")
        whenever(provider.description).thenReturn("description")
    }

    @Test
    fun loadNew() {
        whenever(gameRepository.get(any())).thenReturn(null)
        loader.load()
        verify(gameRepository, times(1)).get(eq(game.name))
        verify(gameRepository, times(1)).create(any())
    }

    @Test
    fun loadExisting() {
        whenever(gameRepository.get(any())).thenReturn(game)
        loader.load()
        verify(gameRepository, times(1)).get(eq(game.name))
        verify(gameRepository, times(0)).create(any())
    }

    @Test
    fun providerLoadsData() {
        whenever(gameRepository.get(any())).thenReturn(game)
        loader.load()
        verify(provider, times(1)).matches()
        verify(provider, times(1)).others()
    }

    @Test
    fun updatedWithMatches() {
        whenever(gameRepository.get(any())).thenReturn(game)
        loader.load()
        verify(gameRepository, times(0)).create(any())
        verify(gameRepository, times(1)).update(game)
    }
}