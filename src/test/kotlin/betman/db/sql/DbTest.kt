package betman.db.sql

import org.jetbrains.exposed.sql.Database
import org.junit.After
import org.junit.Before
import java.sql.Connection


open class DbTest {
    private var connection: Connection? = null

    @Before
    fun loadDb() {
        val connect: Database = Database.connect("jdbc:h2:mem:creation_test", driver = "org.h2.Driver")
        connection = connect.connector()
        DbInitializer().createDb()
    }

    @After
    fun unload() {
        connection?.close()
    }
}