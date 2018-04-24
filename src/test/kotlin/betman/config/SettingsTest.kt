package betman.config

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner


@TestPropertySource(properties = ["db.url=jdbc:h2:mem:test"])
@EnableConfigurationProperties
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [Settings::class])
class SettingsTest {

    @Autowired
    lateinit var settings: Settings

    @Test
    fun getUrl() {
        Assert.assertEquals("jdbc:h2:mem:test", settings.db.url)
    }
}