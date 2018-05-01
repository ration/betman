package betman

import betman.api.provider.GameDataProvider
import betman.db.Database
import betman.db.GameRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Component
class GameDataUpdater @Autowired constructor(providers: Collection<GameDataProvider>,
                                             private val gameRepository: GameRepository,
                                             database: Database) {

    private val logger = KotlinLogging.logger {}

    private val executorService = ScheduledThreadPoolExecutor(4)

    init {
        database.connect()
        database.create()
        for (provider in providers) {
            schedule(provider)
        }
    }

    private fun schedule(provider: GameDataProvider) {
        logger.info("Scheduling {} at interval {}", provider.name, provider.name)
        executorService.scheduleAtFixedRate({ sync(provider) }, 1, provider.updateInterval().seconds, TimeUnit.SECONDS)
    }

    private fun sync(provider: GameDataProvider) {
        logger.info("Starting sync of {}", provider.name)
        DataLoader().load(provider, this.gameRepository)
    }

}