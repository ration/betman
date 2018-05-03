package betman.db

import betman.pojos.Game
import io.reactivex.Maybe
import io.reactivex.Single

interface GameRepository {
    fun games(): Single<List<String>>
    fun create(game: Game): Single<Game>
    fun update(game: Game): Maybe<Game>
    fun get(game: String): Maybe<Game>
}