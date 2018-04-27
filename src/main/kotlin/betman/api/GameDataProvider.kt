package betman.api

import betman.pojos.Match
import betman.pojos.Other

interface GameDataProvider {
    fun matches(): List<Match>
    fun others(): List<Other>
}