package betman.db.exposed

import betman.db.GameRepository
import betman.db.exposed.Games.name
import betman.db.exposed.Matches.externalId
import betman.pojos.Game
import betman.pojos.Match
import betman.pojos.Team
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class ExposedGameRepository : GameRepository {

    override fun games(): List<String> {
        return transaction {
            GameDao.all().map { it.name }
        }
    }

    override fun update(game: Game): Game? {
        return transaction {
            val dao: GameDao? = GameDao.find { name eq game.name }.limit(1).firstOrNull()
            dao?.name = game.name
            dao?.description = game.description

            updateGames(game.matches)
            commit()
            toGame(dao)
        }
    }

    private fun updateGames(matches: List<Match>) {
        for (match in matches) {
            val dao = MatchDao.find { externalId eq match.id }.limit(1).firstOrNull()
            if (dao != null) {
                dao.externalId = match.id
                dao.home = getMatch(match.home)
                dao.away = getMatch(match.away)
                dao.date = match.date.toInstant().toEpochMilli()
                dao.homeGoals = match.homeGoals
                dao.awayGoals = match.awayGoals
            }
        }
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
                date = match.date.toInstant().toEpochMilli()
                homeGoals = match.homeGoals
                awayGoals = match.awayGoals
            }
        }
    }


    private fun getMatch(team: Team): TeamDao {
        return TeamDao.find { Teams.externalId eq team.id }.first()
    }

    private fun createTeams(gameDao: GameDao, teams: Set<Team>) {
        transaction {
            for (team in teams) {
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
            }.firstOrNull()
        }
    }

    private fun toGame(it: GameDao?): Game? {
        if (it != null) {
            return Game(it.id.value,
                    it.name,
                    it.description,
                    matches = getMatches(it.id))
        }
        return null
    }

    private fun getMatches(id: EntityID<Int>): List<Match> {
        return MatchDao.find { Matches.game eq id }.map {
            Match(id = it.externalId,
                    home = getTeam(it.home),
                    away = getTeam(it.away), description = "some text",
                    date = Date.from(Instant.ofEpochMilli(it.date)),
                    homeGoals = it.homeGoals,
                    awayGoals = it.awayGoals)
        }
    }

    private fun getTeam(team: TeamDao): Team {
        return Team(id = team.externalId, name = team.name, iso = team.iso)
    }

}