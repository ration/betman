package betman

import betman.api.GameDataProvider
import betman.db.GameRepository
import betman.pojos.Game

class DataLoader(private val provider: GameDataProvider,
                 private var gameRepository: GameRepository) {

    fun load() {
        val game: Game = gameRepository.get(provider.name)
                ?: gameRepository.create(Game(name = provider.name, description = provider.description))

        val matches = provider.matches()
        val others = provider.others()
        gameRepository.update(game.withData(matches, others))
    }
}