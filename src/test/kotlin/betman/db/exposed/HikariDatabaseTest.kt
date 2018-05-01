package betman.db.exposed

import betman.config.Settings
import betman.db.HikariDatabase
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader


@EnableConfigurationProperties
@TestPropertySource(properties = ["db.url=jdbc:h2:mem:test"])
@RunWith(SpringRunner::class)
@ContextConfiguration(loader = AnnotationConfigContextLoader::class, classes = [HikariDatabase::class, Settings::class])
class HikariDatabaseTest {

    @Autowired
    lateinit var dbFactory: HikariDatabase

    @Test
    fun loadDb() {
        Assert.assertNotNull(dbFactory.datasource)
    }

}