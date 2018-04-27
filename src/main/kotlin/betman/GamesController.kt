package betman

import betman.api.GameAdapter
import betman.pojos.Game
import betman.pojos.Match
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class GamesController {


    @GetMapping("/all")
    fun all(@RequestParam game: Int): List<Match> {
        return listOf()
       // return adapter.regularMatches()
    }

    @GetMapping("/game")
    fun game(@RequestParam game: Int): Game {
        TODO("implement")
    }
}
