package betman.controller

import betman.db.BettingRepository
import betman.pojos.ScoreBet
import betman.pojos.Odds
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger

@RestController
@RequestMapping("/api/bets")
class BettingController {

    @Autowired
    lateinit var repository: BettingRepository

    val LOG = Logger.getLogger(this.javaClass.name)


    @PostMapping("/update", consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    fun addBets(@RequestParam game: Int, @RequestParam user: Long, @RequestBody scoreBets: List<ScoreBet>) {
        LOG.info(String.format("received bets from %s, %s", game, scoreBets.toString()))
    }

    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun bets(@RequestParam game: Int, @RequestParam user: Long): List<ScoreBet> {
        return listOf()
    }

    @GetMapping("/odds", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun odds(game: Int): List<Odds> {
        return repository.odds(game)
    }

}


