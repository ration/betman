package betman.db.exposed

import betman.InvalidUserException
import betman.PointCalculator
import betman.pojos.*
import org.jetbrains.exposed.dao.EntityID
import java.time.Instant
import java.util.*

object Converters {

    val bettingRepository = ExposedBettingRepository()

    fun toGroup(groupDao: GroupDao, gameName: String, userDisplayName: String? = null): Group {
        val userDao = UserDao.findById(groupDao.owner)!!
        val group = Group(id = groupDao.id.value,
                name = groupDao.name,
                description = groupDao.description,
                key = groupDao.key,
                game = gameName,
                admin = userDao.name,
                userDisplayName = userDisplayName,
                winnerPoints = groupDao.winnerPoints,
                goalKingPoints = groupDao.goalKingPoints,
                teamGoalPoints = groupDao.teamGoalPoints,
                exactScorePoints = groupDao.exactScorePoints,
                correctWinnerPoints = groupDao.correctWinnerPoints
        )
        group.standings = getStandings(group)
        return group
    }

    fun toGame(game: GameDao?): Game? {
        if (game != null) {
            return Game(game.id.value,
                    game.name,
                    game.description,
                    matches = getMatches(game.id),
                    teams = getTeams(game.id))
        }
        return null
    }

    private fun getTeams(gameId: EntityID<Int>): List<Team> = TeamDao.find { Teams.game eq gameId }.map { getTeam(it) }


    private fun getStandings(group: Group): List<Score> {
        val users = GroupUserDao.find { GroupUser.group eq group.id }
        val game = GameDao.find { Games.name eq group.game }.firstOrNull()
        if (game != null && group.key != null) {
            val winner = if (game.winner != null) TeamDao.find { Teams.id eq game.winner!! }.map { it.externalId }.firstOrNull() else null


            // Maybe later unblock and streamize this entire build process
            return users.map {
                val user = UserDao.findById(it.user)?.name ?: it.name
                Score(points = PointCalculator.calculate(group,
                        toGame(game)!!, winner, game.goalKing, bettingRepository.get(group.key!!, getUserName(it)).blockingGet()),
                        user = user, displayName = it.name)
            }.sortedByDescending { it.points }
        }
        return listOf()
    }

    private fun getUserName(dao: GroupUserDao): String = UserDao.findById(dao.user)?.name
            ?: throw InvalidUserException()


    private fun getMatches(id: EntityID<Int>): List<Match> {
        return MatchDao.find { Matches.game eq id }.map {
            Match(id = it.externalId,
                    home = it.home?.externalId ?: -1,
                    away = it.away?.externalId ?: -1,
                    description = it.description,
                    date = Date.from(Instant.ofEpochMilli(it.date)),
                    homeGoals = it.homeGoals,
                    awayGoals = it.awayGoals,
                    homeOdds = it.homeOdds?.toDouble(),
                    awayOdds = it.awayOdds?.toDouble()
            )
        }.sortedBy { it.id }
    }

    private fun getTeam(team: TeamDao): Team {
        return Team(id = team.externalId, name = team.name, iso = team.iso)
    }

}