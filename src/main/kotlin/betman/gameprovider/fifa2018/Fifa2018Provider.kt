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
@Component("Fifa2018")
class Fifa2018Provider @Autowired constructor(@Qualifier("FileJsonLoader") private val remote: JsonLoader) : GameDataProvider {

    override fun teams(): Observable<List<Team>> {
        return Observable.just(data.teams?.map { Team(it.id, it.name, it.iso) })
    }


    private final val logger = KotlinLogging.logger {}


    private val gameSubject: BehaviorSubject<List<Match>> = BehaviorSubject.create()
    private val winnerSubject: BehaviorSubject<Team> = BehaviorSubject.create() // TODO ??
    private val goalKingSubject: BehaviorSubject<String> = BehaviorSubject.create() // TODO ??


    override val name: String
        get() = "Fifa2018"
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
            return Match(id = match.name, home = home.id, away = away.id, description = description, date = df.parse(match.date),
                    homeOdds = odds(home.name, away.name)?.homeOdds,
                    awayOdds = odds(home.name, away.name)?.awayOdds, homeGoals = match.homeResult?.toIntOrNull(),
                    awayGoals = match.awayResult?.toIntOrNull())
        }
        return null
    }

    private fun asTeam(id: String?): Team {
        return data.teams?.filter { it.id.toString() == id }?.map { Team(it.id, it.name, it.iso) }?.firstOrNull()
                ?: Team.UNKNOWN_TEAM // We lose the winner from group x type of data here :(
    }

    fun odds(): List<Odds> {
        val odds = """
            Russia 1.39
Saudi Arabia 9.00

Egypt 6.40
Uruguay 1.57

Morocco 2.42
Iran 3.30

Portugal 4.25
Draw 3.40

France 1.20
Australia 17.00

Argentina 1.35
Iceland 11.00

Peru 3.25
Denmark 2.25

Croatia 1.74
Nigeria 5.00

Costa Rica 3.95
Serbia 1.99

Germany 1.42
Mexico 7.60

Brazil 1.36
Switzerland 9.00

Sweden 2.08
South Korea 3.65

Belgium 1.15
Panama 23.00

Tunisia 10.00
England 1.34

Colombia 1.70
Japan 5.20

Poland 2.30
Senegal 3.25

Russia 1.85
Egypt 4.40

Portugal 1.52
Morocco 6.80

Uruguay 1.20
Saudi Arabia 17.00

Iran 17.00
Spain 1.18

Denmark 1.68
Australia 5.30

France 1.41
Peru 7.40

Argentina 1.92
Croatia 4.20

Brazil 1.23
Costa Rica 12.00

Nigeria 2.72
Iceland 2.76

Serbia 2.84
Switzerland 2.62

Belgium 1.29
Tunisia 11.00

South Korea 4.25
Mexico 1.89

Germany 1.45
Sweden 7.00

England 1.18
Panama 17.00

Japan 3.25
Senegal 2.30

Poland 2.84
Colombia 2.52

Saudi Arabia 4.90
Egypt 1.73

Uruguay 2.46
Russia 2.98

Iran 9.00
Portugal 1.35

Spain 1.34
Morocco 9.00

Denmark 5.50
France 1.64

Australia 3.45
Peru 2.14

Iceland 4.70
Croatia 1.78

Nigeria 7.00
Argentina 1.46

South Korea 11.00
Germany 1.27

Mexico 2.52
Sweden 2.88

Switzerland 1.87
Costa Rica 4.40

Serbia 8.60
Brazil 1.35

Japan 4.30
Poland 1.84

Senegal 3.85
Colombia 1.99

Panama 3.30
Tunisia 2.25

England 2.88
Belgium 2.42
"""
        val rows = odds.split("\n" +
                "\n")
        val list = mutableListOf<Odds>()
        for (row in rows) {
            val regex = "(.*?) (\\d+\\.\\d+) (.*?) (\\d+\\.\\d+)".toRegex()

            val match = regex.find(row.trim().replace("\n", " "))
            if (match != null) {
                val split = match.groupValues
                list += Odds(split[1], split[2].toDouble(), split[3], split[4].toDouble())
            }
        }
        return list
    }

    private fun odds(home: String?, away: String?): Odds? = odds().find { it.home == home && it.away == away }

    data class Odds(val home: String, val homeOdds: Double, val away: String, val awayOdds: Double)
}



