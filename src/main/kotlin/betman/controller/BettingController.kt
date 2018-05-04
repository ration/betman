package betman.controller

import betman.db.BettingRepository
import betman.pojos.Bet
import io.reactivex.Maybe
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/bets")
class BettingController {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var repository: BettingRepository

    @PostMapping("/update", consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    fun addBets(@RequestParam game: String, @RequestBody bet: Bet, user: Principal) {
        logger.debug("Received request to game {}", game)
        repository.bet(game, bet, user.name)
    }

    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun bets(@RequestParam game: String, user: Principal): Maybe<Bet> {
        return repository.get(game, user.name)
    }


}


