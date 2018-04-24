package betman.db.sql

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DbInitializer @Autowired constructor() {



    fun createDb() {
        transaction {
            SchemaUtils.create(Games, Odds, Teams)
        }
    }

}