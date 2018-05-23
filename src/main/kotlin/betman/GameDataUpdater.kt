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
                                             database: Database) {

    private val logger = KotlinLogging.logger {}
    private val loader = DataLoader()

    init {
        database.connect()
        database.create()
        for (provider in providers) {
            loader.subscribe(provider, gameRepository)
        }
    }
}