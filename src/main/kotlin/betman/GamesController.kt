package betman

import betman.db.GameRepository
import betman.pojos.Game
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class GamesController @Autowired constructor(private val gameRepository: GameRepository) {


    @GetMapping("/games")
    fun all(): List<String> {
        return gameRepository.games()
    }

    @GetMapping("/games/{game}")
    fun game(@PathVariable game: String): Game? {
        return gameRepository.get(game)
    }
}
