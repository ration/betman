package betman

import betman.lsv.Lsv
import betman.lsv.LsvAdapter
import betman.pojos.Game
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class GamesController {

    private val lsvAdapter = LsvAdapter()

    @GetMapping("/all")
    fun all(): List<Game> {
        return lsvAdapter.regularGames()
    }

    @GetMapping("/lsv")
    fun lsv(): Lsv {
        return lsvAdapter.data
    }
}
