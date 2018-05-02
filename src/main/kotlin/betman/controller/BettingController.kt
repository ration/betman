package betman.controller

import betman.db.BettingRepository
import betman.pojos.ScoreBet
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bets")
class BettingController {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var repository: BettingRepository

    @PostMapping("/update", consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    fun addBets(@RequestParam game: Int, @RequestParam user: Long, @RequestBody scoreBets: List<ScoreBet>) {
        logger.debug("Received data from {} {}", game, scoreBets.toString())
    }

    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun bets(game: Int, user: Long): List<ScoreBet> {
        return listOf()
    }


}


