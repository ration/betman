package betman.db.exposed


import org.jetbrains.exposed.dao.IntIdTable

object Games : IntIdTable() {
    val name = varchar("name", 50).uniqueIndex()
    val description = varchar("description", 1024)
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
    val key = varchar("key", 32).uniqueIndex()
    val game = reference("game", Games)
}

object Users : IntIdTable() {
    val name = varchar("name", 50).uniqueIndex()
    val password = varchar("password", 4096)
}

object GroupUser : IntIdTable(name = "users_groups") {
    val user = reference("user", Users).primaryKey(2)
    val group = reference("group", Groups).primaryKey(3)
    val name = varchar("name", 50).index()
}


object Bets : IntIdTable() {
    val match = reference("match", Matches).primaryKey(2)
    val user = reference("user", Users)
    val home = integer("home")
    val away = integer("away")
}
