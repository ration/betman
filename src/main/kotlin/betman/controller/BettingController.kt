package betman.controller

import betman.db.BettingRepository
import betman.pojos.ScoreBet
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class BettingController {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var repository: BettingRepository

    @PostMapping("/bets/update", consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    fun addBets(game: Int, user: Long, @RequestBody scoreBets: List<ScoreBet>) {
        logger.debug("Received data from {} {}", game, scoreBets.toString())
    }

    @GetMapping("bets", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun bets(game: Int, user: Int): List<ScoreBet> {
        return listOf()
    }


}


