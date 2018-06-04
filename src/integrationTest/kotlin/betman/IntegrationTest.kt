package betman

import org.jetbrains.exposed.sql.Database
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import java.sql.Connection

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [(Application::class)])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource(locations = ["classpath:application-integration.properties"])
abstract class IntegrationTest {

    @Autowired
    protected lateinit var client: WebTestClient

    companion object {

        var connection: Connection? = null

        @BeforeClass
        @JvmStatic
        fun init() {
            if (connection == null) {
                val db = Database.connect("jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE", driver = "org.h2.Driver", user = "betman", password = "manbet")
                connection = db.connector.invoke()
            }
        }

    }


}