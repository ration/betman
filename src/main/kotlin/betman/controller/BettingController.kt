package betman.controller

import betman.FootballGuesser
import betman.db.BettingRepository
import betman.db.GameRepository
import betman.db.GroupRepository
import betman.pojos.Bet
import io.reactivex.Maybe
import io.reactivex.Single
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


    @Autowired
    lateinit var gameRepository: GameRepository

    @Autowired
    lateinit var groupRepository: GroupRepository


    /**
     * @param group game key
     */
    @PostMapping("/update", consumes = [(MediaType.APPLICATION_JSON_VALUE)])
    fun addBets(@RequestParam group: String, @RequestBody bet: Bet, user: Principal): Single<Bet> {
        logger.debug("Received request to game {}", group)
        return repository.bet(group, bet, user.name)
    }

    /**
     * @param group game key
     */
    @GetMapping("/{group}", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun bets(@PathVariable group: String,
             user: Principal): Maybe<Bet> {
        return repository.get(group, user.name)
    }

    /**
     * @param group game key
     */
    @GetMapping("/{group}/{user}", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun bets(@PathVariable group: String,
             @PathVariable user: String): Maybe<Bet> {
        return repository.get(group, user)
    }

    /** The api is dumb. The client returns a list of bets but we nuke it */
    @PostMapping("/guess/{key}")
    fun guess(@PathVariable key: String, @RequestBody bet: Bet, user: Principal): Maybe<Bet> {
        return groupRepository.get(key, user.name).flatMap { gameRepository.get(it.game) }.map { game ->
            for (score in bet.scores) {
                score.home = FootballGuesser.guess(game.matches.find { it.id == score.matchId }?.homeOdds)
                score.away = FootballGuesser.guess(game.matches.find { it.id == score.matchId }?.awayOdds)
            }
            repository.bet(key, bet, user.name)
            bet
        }
    }
}




