package betman.db.sql

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class DbInitializer {
    fun createDb() {
        transaction {
            SchemaUtils.create(Matches, Odds, Teams, Groups, Users, GroupUser, Games)
        }
    }
}