package betman.db.sql

import org.jetbrains.exposed.sql.Database
import org.junit.Test

class DbInitializerTest {

    @Test
    fun createDb() {
        Database.connect("jdbc:h2:mem:creation_test", driver = "org.h2.Driver")
        DbInitializer().createDb()
    }
}