package betman

import betman.api.provider.GameDataProvider
import betman.db.GameRepository
import betman.pojos.Game
import betman.pojos.Match
import betman.pojos.Team
import io.reactivex.disposables.CompositeDisposable
import mu.KotlinLogging
import org.springframework.stereotype.Component

/**
 * Loads data from game repository
 */
@Component
class DataLoader {
    private val logger = KotlinLogging.logger {}
    private val subscriptions = CompositeDisposable()


    fun subscribe(provider: GameDataProvider,
                  gameRepository: GameRepository) {
        try {
            subscriptions.add(provider.matches().subscribe { matches ->
                logger.info("Got list of matches from ${provider.name}")
                gameRepository.get(provider.name).subscribe({ _ ->
                    logger.info("Updating database")
                    gameRepository.update(toGame(provider, matches))

                    logger.info("Db sync done")
                }, { e ->
                    logger.error("Error in subscriber", e)
                }, {
                    logger.info("Game did not exist. Creating")
                    provider.teams().subscribe { teams ->
                        gameRepository.create(toGame(provider, matches, teams))
                    }
                })
            })
        } catch (e: Exception) {
            logger.error("Error during sync", e)
        }
    }

    private fun toGame(provider: GameDataProvider, matches: List<Match>,  teams: List<Team> = listOf()) =
            Game(name = provider.name, description = provider.description,
                    matches = matches, teams = teams)


}