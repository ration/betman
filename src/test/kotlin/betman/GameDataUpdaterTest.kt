package betman

import betman.api.provider.GameDataProvider
import betman.db.GameRepository
import betman.db.exposed.DbFactory
import com.nhaarman.mockito_kotlin.whenever
import com.zaxxer.hikari.HikariDataSource
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.time.Duration

class GameDataUpdaterTest {

    @Mock
    lateinit var provider: GameDataProvider

    @Mock
    lateinit var repo: GameRepository

    @Mock
    lateinit var dbFactory: DbFactory

    @Mock
    lateinit var dataSource: HikariDataSource

    lateinit var updater: GameDataUpdater

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        whenever(dbFactory.datasource()).thenReturn(dataSource)
        whenever(provider.updateInterval()).thenReturn(Duration.ofSeconds(1))
        updater = GameDataUpdater(listOf(provider), repo, dbFactory)
    }

    @Test
    fun constructor() {
        assertNotNull(updater)
    }
}