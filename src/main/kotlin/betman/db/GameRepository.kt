package betman.db

import betman.pojos.Game

interface GameRepository {
    fun games(game: Int) : List<Game>
}