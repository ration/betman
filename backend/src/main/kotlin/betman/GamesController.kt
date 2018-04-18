package betman

import betman.lsv.LsvAdapter
import betman.pojos.Game
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GamesController {

    private val lsvAdapter = LsvAdapter()

    @GetMapping("/all")
    fun all(): List<Game> {
        return lsvAdapter.regularGames()
    }
}
