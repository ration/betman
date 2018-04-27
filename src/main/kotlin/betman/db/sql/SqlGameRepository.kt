package betman.db.sql

import betman.db.GameRepository
import betman.pojos.Game
import org.jetbrains.exposed.sql.transactions.transaction

class SqlGameRepository: GameRepository {
    override fun create(game: Game): Game {
        return transaction {
            val game: GameDao = GameDao.new {
                name = game.name
                description = game.description
            }
            commit()
            Game(game.id.value, game.name, game.description)
        }
    }

    override fun games(game: Int): List<Game> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}