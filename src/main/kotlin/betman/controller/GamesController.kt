package betman.controller

import betman.db.GameRepository
import betman.pojos.Game
import io.reactivex.Maybe
import io.reactivex.Single
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/games")
class GamesController @Autowired constructor(private val gameRepository: GameRepository) {


    @GetMapping("")
    fun all(): Single<List<String>> {
        return gameRepository.games()
    }

    @Secured("USER")
    @GetMapping("/{game}")
    fun game(@PathVariable game: String): Maybe<Game> {
        return gameRepository.get(game)
    }
}
