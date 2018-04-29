package betman.db.sql

import betman.db.GameRepository
import betman.db.sql.Games.name
import betman.pojos.Game
import betman.pojos.Match
import betman.pojos.Team
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component
import java.util.*

@Component
class SqlGameRepository : GameRepository {
    override fun update(game: Game): Game {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(game: Game): Game {
        return transaction {
            val newGame: GameDao = GameDao.new {
                name = game.name
                description = game.description
            }
            val teams = game.matches.flatMap { listOf(it.home, it.away) }.toSet()
            createTeams(newGame, teams)
            addMatches(newGame, game.matches)
            commit()
            Game(newGame.id.value, newGame.name, newGame.description, matches = game.matches)
        }
    }

    private fun addMatches(gameDao: GameDao, matches: List<Match>) {
        for (match in matches) {
            MatchDao.new {
                externalId = match.id
                game = gameDao
                home = getMatch(match.home)
                away = getMatch(match.away)
            }
        }
    }

    private fun getMatch(team: Team): TeamDao {
        return TeamDao.find { Teams.externalId eq team.id }.first()
    }

    private fun createTeams(gameDao: GameDao, teams: Set<Team>) {
        for (team in teams) {
            transaction {
                TeamDao.new {
                    game = gameDao
                    name = team.name
                    iso = team.iso
                    externalId = team.id
                }
            }
        }
    }

    override fun get(game: String): Game? {
        return transaction {
            GameDao.find { name eq game }.limit(1).map {
                toGame(it)
            }.first()
        }
    }

    private fun toGame(it: GameDao): Game {
        return Game(it.id.value,
                it.name,
                it.description,
                matches = getMatches(it.id))

    }

    private fun getMatches(id: EntityID<Int>): List<Match> {
        return MatchDao.find { Matches.game eq id }.map {
            Match(id = it.externalId,
                    home = getTeam(it.home),
                    away = getTeam(it.away), description = "some text",
                    date = Date())
        }
    }

    private fun getTeam(team: TeamDao): Team {
        return Team(id = team.externalId, name = team.name, iso = team.iso)
    }

}