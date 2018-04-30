package betman.api.provider

import betman.pojos.Match
import betman.pojos.Other
import java.time.Duration

interface GameDataProvider {
    fun updateInterval(): Duration {
        return Duration.ofHours(1)
    }
    fun matches(): List<Match>
    fun others(): List<Other>
    val name: String
    val description: String
}