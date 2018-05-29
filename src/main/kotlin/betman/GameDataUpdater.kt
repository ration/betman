package betman

import betman.api.provider.GameDataProvider
import betman.db.Database
import betman.db.GameRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GameDataUpdater @Autowired constructor(providers: Collection<GameDataProvider>,
                                             gameRepository: GameRepository,
                                             database: Database,
                                             loader: DataLoader) {
    private val logger = KotlinLogging.logger {}

    init {
        logger.info("Creating database connections")
        database.connect()
        database.create()
        logger.info("Subscribing to ${providers.size} providers")
        for (provider in providers) {
            loader.subscribe(provider, gameRepository)
        }
    }
}