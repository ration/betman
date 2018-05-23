package betman

import betman.api.provider.GameDataProvider
import betman.db.Database
import betman.db.GameRepository
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class GameDataUpdaterTest {

    @Mock
    lateinit var provider: GameDataProvider

    @Mock
    lateinit var repo: GameRepository

    @Mock
    lateinit var database: Database

    @Mock
    lateinit var loader: DataLoader

    lateinit var updater: GameDataUpdater

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun constructor() {
        updater = GameDataUpdater(listOf(provider), repo, database, loader)
        verify(loader, times(1)).subscribe(provider, repo)
    }
}