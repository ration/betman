package betman.db

import betman.pojos.Game

interface GameRepository {
    fun games(): List<String>
    fun create(game: Game): Game
    fun update(game: Game): Game?
    fun get(game: String): Game?
}