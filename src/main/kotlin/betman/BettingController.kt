package betman

import betman.db.BettingRepository
import betman.lsv.LsvAdapter
import betman.pojos.Bet
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
    var userBets: List<Bet> = LsvAdapter().regularGames().map { Bet(it.id, 2, 3) }


    @PostMapping("/bets/update", consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    fun addBets(game: Int, user: Long, @RequestBody bets: List<Bet>) {
        LOG.info(String.format("received bets from %s, %s", game, bets.toString()))
        this.userBets = bets
    }

    @GetMapping("bets", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun bets(game: Int, user: Long): List<Bet> {
        // TODO actually from dao
        return userBets
    }

    fun odds(game: Int): List<Odds> {
        return repository.odds(game)
    }

}


