package betman.db.exposed

import betman.RxUtils.maybeNull
import betman.db.GameRepository
import betman.db.exposed.Games.name
import betman.db.exposed.Matches.externalId
import betman.pojos.Game
import betman.pojos.Match
import betman.pojos.Team
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Maybes
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component


@Component
class ExposedGameRepository : GameRepository {

    override fun games(): Single<List<String>> {
        return Observable.just(transaction {
            GameDao.all().map { it.name }
        }).singleOrError()
    }


    override fun update(game: Game): Maybe<Game> {
        return Maybes.maybeNull(
                transaction {
                    val dao: GameDao? = GameDao.find { name eq game.name }.limit(1).firstOrNull()
                    dao?.name = game.name
                    dao?.description = game.description

                    updateGames(game.matches)
                    commit()
                    Converters.toGame(dao)
                })
    }

    private fun updateGames(matches: Map<Int, Match>) {
        for (match in matches.values) {
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

    override fun create(game: Game): Single<Game> {
        return Observable.just(transaction {
            val newGame: GameDao = GameDao.new {
                name = game.name
                description = game.description
            }
            val teams = game.matches.values.flatMap { listOf(it.home, it.away) }.toSet()
            createTeams(newGame, teams)
            addMatches(newGame, game.matches)
            commit()
            Game(newGame.id.value, newGame.name, newGame.description, matches = game.matches)
        }).singleOrError()
    }

    private fun addMatches(gameDao: GameDao, matches: Map<Int, Match>) {
        for (match in matches.values) {
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

    override fun get(game: String): Maybe<Game> {
        return Maybes.maybeNull(transaction {
            GameDao.find { name eq game }.limit(1).map {
                converter.toGame(it)
            }.firstOrNull()
        })
    }


}