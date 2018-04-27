package betman

import betman.db.BettingRepository
import betman.gameprovider.fifa2018.Fifa2018Provider
import betman.pojos.ScoreBet
import betman.pojos.Odds
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger

@RestController
@RequestMapping("/api")
class BettingController {

    @Autowired
    lateinit var repository: BettingRepository

    val LOG = Logger.getLogger(this.javaClass.name)


    @PostMapping("/bets/update", consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    fun addBets(game: Int, user: Long, @RequestBody scoreBets: List<ScoreBet>) {
        LOG.info(String.format("received bets from %s, %s", game, scoreBets.toString()))
    }

    @GetMapping("bets", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun bets(game: Int, user: Long): List<ScoreBet> {
        return listOf()
    }

    fun odds(game: Int): List<Odds> {
        return repository.odds(game)
    }

}


