package betman.gameprovider.fifa2018

import betman.api.JsonLoader
import betman.api.provider.GameDataProvider
import betman.pojos.Match
import betman.pojos.Team
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import mu.KotlinLogging
import org.codehaus.jackson.map.util.StdDateFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 * Provider for  WorldCup data via LSV "https://raw.githubusercontent.com/lsv/fifa-worldcup-2018/master/data.json"
 */
@Component("XXFifa2018") // TODO back to fifa, fix that by lazy stuff, doesn't work. Should always just reload
class Fifa2018Provider @Autowired constructor(@Qualifier("FileJsonLoader") private val remote: JsonLoader) : GameDataProvider {

    override fun teams(): Observable<List<Team>> {
        return Observable.just(data.teams?.map { Team(it.id, it.name, it.iso) })
    }


    private final val logger = KotlinLogging.logger {}


    private val gameSubject: BehaviorSubject<List<Match>> = BehaviorSubject.create()
    private val winnerSubject: BehaviorSubject<Team> = BehaviorSubject.create() // TODO ??
    private val goalKingSubject: BehaviorSubject<String> = BehaviorSubject.create() // TODO ??


    override val name: String
        get() = "XXFifa2018"
    override val description: String
        get() = "Fifa 2018 World Cup"


    init {
        Observable.interval(0, 1, TimeUnit.HOURS).subscribe { loadMatches() }
    }

    private val data: Lsv by lazy {
        loadData()
    }

    private fun loadData(): Lsv {
        return remote.fetch("https://raw.githubusercontent.com/lsv/fifa-worldcup-2018/master/data.json", Lsv::class.java)
                ?: throw RuntimeException("Failed to load external url data")
    }

    companion object {
        const val REGULAR_GAMES = 48
        const val PLAYOFF_GAMES = 16
    }


    override fun winner(): Observable<Team> {
        return winnerSubject.hide()
    }

    override fun goalKing(): Observable<String> {
        return goalKingSubject.hide()
    }


    override fun matches(): Observable<List<Match>> {
        return gameSubject.hide()
    }

    private fun loadMatches() {
        logger.info("Loading Fifa data")
        val data = loadData()
        if (data.teams != null) {
            val list = (mapGames("Group A", data.groups.a.matches) +
                    mapGames("Group B", data.groups.b.matches) +
                    mapGames("Group C", data.groups.c.matches) +
                    mapGames("Group D", data.groups.d.matches) +
                    mapGames("Group E", data.groups.e.matches) +
                    mapGames("Group F", data.groups.f.matches) +
                    mapGames("Group G", data.groups.g.matches) +
                    mapGames("Group H", data.groups.h.matches) +
                    mapGames("Round of 16", data.knockout.round16.matches) +
                    mapGames("Quarter Finals", data.knockout.round8.matches) +
                    mapGames("Semi Finals", data.knockout.round4.matches) +
                    mapGames("Bronze Game", data.knockout.round2Loser.matches) +
                    mapGames("Final", data.knockout.round2.matches)).sortedBy { it.id }
            gameSubject.onNext(list)
        }
    }


    private fun mapGames(description: String, matches: List<MatchesItem>?): List<Match> {
        if (matches == null) {
            return listOf()
        }
        return matches.mapNotNull { match -> asGame(description, match) }.toList()
    }

    private fun asGame(description: String, match: MatchesItem?): Match? {
        if (match != null && data.teams != null) {
            val home = asTeam(match.homeTeam)
            val away = asTeam(match.awayTeam)
            val df = StdDateFormat()
            return Match(id = match.name, home = home.id, away = away.id, description = description, date = df.parse(match.date))
        }
        return null
    }

    private fun asTeam(id: String?): Team {
        return data.teams?.filter { it.id.toString() == id }?.map { Team(it.id, it.name, it.iso) }?.firstOrNull()
                ?: Team.UNKNOWN_TEAM // We lose the winner from group x type of data here :(
    }
}



