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

    /**
     * @param group game key
     */
    @PostMapping("/update", consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    fun addBets(@RequestParam group: String, @RequestBody bet: Bet, user: Principal) {
        logger.debug("Received request to game {}", group)
        repository.bet(group, bet, user.name)
    }

    /**
     * @param group game key
     */
    @GetMapping("/{group}", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun bets(@PathVariable group: String,
             user: Principal): Maybe<Bet> {
        return repository.get(group, user.name)
    }


}


