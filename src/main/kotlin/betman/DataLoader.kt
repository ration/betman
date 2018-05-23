package betman

import betman.api.provider.GameDataProvider
import betman.db.GameRepository
import betman.pojos.Game
import io.reactivex.disposables.CompositeDisposable
import mu.KotlinLogging

/**
 * Loads data from game repository
 */
class DataLoader {
    private val logger = KotlinLogging.logger {}
    private val subscriptions = CompositeDisposable()


    fun subscribe(provider: GameDataProvider,
                  gameRepository: GameRepository) {
        try {
            subscriptions.add(provider.matches().subscribe { matches ->
                logger.info("Got list of matches")
                gameRepository.get(provider.name).subscribe({ _ ->
                    logger.info("Updating database")
                    gameRepository.update(Game(name = provider.name, description = provider.description,
                            matches = matches))

                    logger.info("Db sync done")
                }, { e ->
                    logger.error("Error in subscriber", e)
                }, {
                    logger.info("Game did not exist. Creating")
                    gameRepository.create(Game(name = provider.name, description = provider.description,
                            matches = matches))
                })
            })
        } catch (e: Exception) {
            logger.error("Error during sync", e)
        }
    }


}