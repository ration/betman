package betman.db.exposed


import org.jetbrains.exposed.dao.IntIdTable

object Games : IntIdTable() {
    val name = varchar("name", 50).uniqueIndex()
    val description = varchar("description", 1024)
    val winner = integer("winner").nullable() // <- cyclic reference not possible
    val goalKing = varchar("goal_king", 1024).nullable()
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
    val description = varchar("description", 250).nullable()
    val key = varchar("key", 32).uniqueIndex()
    val game = reference("game", Games)
    val winnerPoints = integer("winner_points")
    val goalKingPoints = integer("goal_king_points")
    val teamGoalPoints = integer("team_goal_points")
    val exactScorePoints = integer("exact_score_points")
    val owner = reference("owner", Users)
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
    val match = reference("match", Matches).nullable()
    val winner = reference("team", Teams).nullable()
    val goalking = varchar("goal_king", 1024).nullable()
    val user = reference("user", Users)
    val home = integer("home")
    val away = integer("away")
    val group = reference("group", Groups)
}
