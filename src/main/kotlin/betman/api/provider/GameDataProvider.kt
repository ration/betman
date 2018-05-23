package betman.api.provider

import betman.pojos.Match
import betman.pojos.Team
import io.reactivex.Observable

interface GameDataProvider {
    fun matches(): Observable<List<Match>>
    fun winner(): Observable<Team>
    fun goalKing(): Observable<String>
    val name: String
    val description: String
}