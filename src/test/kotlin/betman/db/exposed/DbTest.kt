package betman.db.exposed

import betman.config.Settings
import betman.db.HikariDatabase
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import java.sql.Connection


open class DbTest {
    private lateinit var connection: Connection
    private lateinit var factory: HikariDatabase

    @Before
    fun loadDb() {
        val jdbcUrl = "jdbc:h2:mem:creation_test"
        val settings = Settings()
        settings.db.url = jdbcUrl
        factory = HikariDatabase(settings)
        connection = factory.connect()
        factory.create()
    }


    @After
    fun unload() {
        connection.close()
        factory.close()
    }

    protected fun createGame(): GameDao = transaction {
        GameDao.new {
            name = "test"
            description = "description"
        }
    }

    protected fun createUser(userName: String) = transaction {
        UserDao.new {
            name = userName
            password = "password"
        }
    }
}