package betman

import betman.pojos.Bet
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
@RequestMapping("/api")
class BettingController {
    val LOG = Logger.getLogger(this.javaClass.name)

    @PostMapping("/addBets", consumes=[(MediaType.APPLICATION_JSON_VALUE)])
    fun addBets(game:Int, @RequestBody bets: List<Bet>) {
        LOG.info(String.format("received bets from %s", game))
    }

}
