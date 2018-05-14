package betman.db.exposed

import betman.PointCalculator
import betman.pojos.*
import org.jetbrains.exposed.dao.EntityID
import java.time.Instant
import java.util.*

object Converters {
    fun toGroup(group: GroupDao, gameName: String, userDisplayName: String? = null): Group {
        val group = Group(id = group.id.value,
                name = group.name,
                description = group.description,
                key = group.key,
                game = gameName,

                userDisplayName = userDisplayName,
                winnerPoints = group.winnerPoints,
                goalKingPoints = group.goalKingPoints,
                teamGoalPoints = group.teamGoalPoints,
                exactScorePoints = group.exactScorePoints
        )
        group.standings = getStandings(group)
        return group
    }

    fun toGame(game: GameDao?): Game? {
        if (game != null) {
            return Game(game.id.value,
                    game.name,
                    game.description,
                    matches = getMatches(game.id))
        }
        return null
    }

    private fun getStandings(group: Group): Standings? {
        val users = GroupUserDao.find { GroupUser.group eq group.id }
        val game = GameDao.find { Games.name eq group.game }.firstOrNull()
        if (game != null) {
            val winner: Int? = game.winner.let { cur ->
                val team: TeamDao? = TeamDao.findById(cur!!)
                team?.externalId
            }
            users.map { Score(points = PointCalculator.calculate(group, toGame(game)!!, winner, game.goalKing, bet)) }
        }
    }


    private fun getMatches(id: EntityID<Int>): Map<Int, Match> {
        return MatchDao.find { Matches.game eq id }.map {
            Pair(it.externalId, Match(id = it.externalId,
                    home = getTeam(it.home),
                    away = getTeam(it.away), description = "some text",
                    date = Date.from(Instant.ofEpochMilli(it.date)),
                    homeGoals = it.homeGoals,
                    awayGoals = it.awayGoals))
        }.toMap()
    }

    private fun getTeam(team: TeamDao): Team {
        return Team(id = team.externalId, name = team.name, iso = team.iso)
    }

}