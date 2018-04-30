package betman.api.provider

import betman.pojos.Match
import betman.pojos.Other
import betman.pojos.Team

/** Interface for provider for game, such as the world cup, data */
interface GameAdapter {
    fun regularMatches(): List<Match>
    fun otherBets(): List<Other>
    fun teams(): List<Team>
}
