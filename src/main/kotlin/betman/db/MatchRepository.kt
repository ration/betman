package betman.db

import betman.pojos.Match

interface MatchRepository {
    fun create(match: Match)
    fun get(id: Int)
}