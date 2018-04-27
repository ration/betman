package betman.db.sql


import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

open class ManualIntIdTable(name: String = "", columnName: String = "id") : IdTable<Int>(name) {
    override val id: Column<EntityID<Int>> = integer(columnName).autoIncrement().entityId()
}

object Odds : IntIdTable() {
    val home = decimal("home", 5, 2)
    val away = decimal("away", 5, 2)
    val game = reference("game", Games)
}

object Games : ManualIntIdTable() {
    val home = reference("home", Teams)
    val away = reference("away", Teams)
    // TODO dates, other data
}

object Teams : ManualIntIdTable() {
    val name = varchar("name", 50).index()
}

object Groups : IntIdTable() {
    val name = varchar("name", 50).index()
    val description = varchar("description", 250).index()
    val key = varchar("key", 32).index()

}


object Users : IntIdTable() {
    val name = varchar("name", 50).index()
    val password = varchar("password", 50).index()
}

object GroupUser : Table(name = "users_groups") {
    val user = reference("user", Users).primaryKey(0)
    val group = reference("group", Groups).primaryKey(1)
    val name = varchar("name", 50).index()
}

