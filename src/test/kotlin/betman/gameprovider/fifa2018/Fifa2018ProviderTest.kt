package betman.gameprovider.fifa2018

import betman.FileJsonLoader
import betman.api.JsonLoader
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class Fifa2018ProviderTest {

    @Mock
    @Qualifier("FileJsonLoader")
    lateinit var loader: JsonLoader

    @InjectMocks
    lateinit var provider: Fifa2018Provider


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        this.javaClass.getResource("/pregame.json").openStream().use {
            val lsv = FileJsonLoader.load(it, Lsv::class.java)
            whenever(loader.fetch(any(), eq(Lsv::class.java))).thenReturn(lsv)
        }
    }

    @Test
    fun matches() {
        provider.matches().subscribe { games ->
            Assert.assertEquals(Fifa2018Provider.REGULAR_GAMES + Fifa2018Provider.PLAYOFF_GAMES, games.size)
            Assert.assertEquals("Russia", games[0].home.name)
        }
    }
}