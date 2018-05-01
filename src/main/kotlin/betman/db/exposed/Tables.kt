package betman.db.exposed


import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

object Games : IntIdTable() {
    val name = varchar("name", 50).uniqueIndex()
    val description = varchar("description", 1024)
}


object Odds : IntIdTable() {
    val home = decimal("home", 5, 2)
    val away = decimal("away", 5, 2)
    val game = reference("game", Matches)
}

object Matches : IntIdTable() {
    val externalId = integer("external_id")
    val game = reference("game", Games)
    val home = reference("home", Teams)
    val away = reference("away", Teams)
    val homeGoals = integer("home_goals").nullable()
    val awayGoals = integer("away_goals").nullable()
    val date = long("date")
}

object Teams : IntIdTable() {
    val game = reference("game", Games)
    val externalId = integer("external_id")
    val name = varchar("name", 50).index()
    var iso = varchar("iso", 10)
}

object Groups : IntIdTable() {
    val name = varchar("name", 50).index()
    val description = varchar("description", 250).index()
    val key = varchar("key", 32).index()
}

object Users : IntIdTable() {
    val name = varchar("name", 50).uniqueIndex()
    val password = varchar("password", 50)
}

object GroupUser : Table(name = "users_groups") {
    val user = reference("user", Users).primaryKey(0)
    val group = reference("group", Groups).primaryKey(1)
    val name = varchar("name", 50).index()
}

object GroupGame : Table(name = "groups_games") {
    val game = reference("game", Games).primaryKey(0)
    val group = reference("group", Groups).primaryKey(1)
}

