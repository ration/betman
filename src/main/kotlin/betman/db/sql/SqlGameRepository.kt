package betman.db.sql

import betman.db.GameRepository
import betman.db.sql.Games.name
import betman.pojos.Game
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

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
            commit()
            Game(newGame.id.value, newGame.name, newGame.description)
        }
    }

    override fun get(game: String): Game? {
        return transaction {
            GameDao.find { name eq game }.limit(1).map { Game(it.id.value, it.name, it.description) }.first()
        }
    }

}