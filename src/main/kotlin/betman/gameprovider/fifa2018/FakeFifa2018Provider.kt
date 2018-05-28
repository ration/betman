package betman.gameprovider.fifa2018

import betman.api.JsonLoader
import betman.api.provider.GameDataProvider
import betman.pojos.Match
import betman.pojos.Team
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Provider for  WorldCup data via LSV "https://raw.githubusercontent.com/lsv/fifa-worldcup-2018/master/data.json"
 */
@Component("Fifa2018")
class FakeFifa2018Provider @Autowired constructor(@Qualifier("FileJsonLoader") private val remote: JsonLoader) : GameDataProvider {
    override fun teams(): Observable<List<Team>> {
        return Observable.just(data.teams?.map { Team(it.id, it.name, it.iso) })
    }

    private final val logger = KotlinLogging.logger {}


    private val gameSubject: BehaviorSubject<List<Match>> = BehaviorSubject.create()
    private val winnerSubject: BehaviorSubject<Team> = BehaviorSubject.create() // TODO ??
    private val goalKingSubject: BehaviorSubject<String> = BehaviorSubject.create() // TODO ??


    private var counter = 0
    private var date = Instant.now()
    override val name: String
        get() = "Fifa2018"
    override val description: String
        get() = "Fifa 2018 World Cup"

    private lateinit var matchList: List<Match>
    private val data: Lsv =
            loadData()

    init {
        matchList = (mapGames("Group A", data.groups.a.matches) +
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
        Observable.interval(0, 1, TimeUnit.MINUTES).subscribe { loadMatches() }

    }


    private fun loadData(): Lsv {
        return remote.fetch("https://raw.githubusercontent.com/lsv/fifa-worldcup-2018/master/data.json", Lsv::class.java)
                ?: throw RuntimeException("Failed to load external url data")
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
        val r = Random()
        for (match in matchList) {
            if (match.date.before(Date())) {
                match.homeGoals = r.nextInt(5)
                match.awayGoals = r.nextInt(5)
            }
        }
        gameSubject.onNext(matchList)
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
            ++counter
            date = date.plus(1, ChronoUnit.MINUTES)
            return Match(id = match.name, home = home.id, away = away.id, description = description, date = Date.from(date))
        }
        return null
    }

    private fun asTeam(id: String?): Team {
        return data.teams?.filter { it.id.toString() == id }?.map { Team(it.id, it.name, it.iso) }?.firstOrNull()
                ?: Team.UNKNOWN_TEAM
    }
}



