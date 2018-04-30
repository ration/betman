package betman.db.exposed

import betman.config.Settings
import org.junit.After
import org.junit.Before
import java.sql.Connection


open class DbTest {
    private lateinit var connection: Connection
    private lateinit var factory: DbFactory

    @Before
    fun loadDb() {
        val jdbcUrl = "jdbc:h2:mem:creation_test"
        val settings = Settings()
        settings.db.url = jdbcUrl
        factory = DbFactory(settings)
        connection = factory.connect()

        factory.createDb(factory.datasource)

    }


    @After
    fun unload() {
        connection.close()
        factory.close()
    }
}