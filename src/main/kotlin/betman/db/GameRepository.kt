package betman.db

import betman.pojos.Game

interface GameRepository {
    fun create(game: Game): Game
    fun games(game: Int) : List<Game>
}