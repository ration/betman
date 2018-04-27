package betman

import betman.gameprovider.fifa2018.Lsv
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [FileJsonLoader::class])
class FileJsonLoaderTest {

    @Autowired
    private lateinit var loader: FileJsonLoader

    @Test
    fun load() {
        val lsv = loader.fetch("classpath:pregame.json", Lsv::class.java)
        assertNotNull(lsv)
    }
}