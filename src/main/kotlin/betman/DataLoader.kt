package betman

import betman.api.provider.GameDataProvider
import betman.db.GameRepository
import betman.pojos.Game
import mu.KotlinLogging

/**
 * Loads data from game repository
 */
class DataLoader {
    private val logger = KotlinLogging.logger {}

    fun load(provider: GameDataProvider,
             gameRepository: GameRepository) {
        try {
            gameRepository.get(provider.name).subscribe({ _ ->
                logger.info("Updating database")
                gameRepository.update(Game(name = provider.name, description = provider.description,
                        matches = provider.matches(), other = provider.others()))

                logger.info("Db sync done")
            }, { e ->
                logger.error("Error in subscriber", e)
            }, {
                logger.info("Game did not exist. Creating")
                gameRepository.create(Game(name = provider.name, description = provider.description,
                        matches = provider.matches(), other = provider.others()))
            })
        } catch (e: Exception) {
            logger.error("Error during sync", e)
        }
    }


}