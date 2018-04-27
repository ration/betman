package betman.gameprovider.fifa2018

import betman.api.GameDataProvider
import betman.api.JsonLoader
import betman.pojos.Match
import betman.pojos.Other
import betman.pojos.Team
import com.fasterxml.jackson.databind.util.ISO8601DateFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

/**
 * Provider for  WorldCup data via LSV "https://raw.githubusercontent.com/lsv/fifa-worldcup-2018/master/data.json"
 */
@Component("Fifa2018")
class Fifa2018Provider : GameDataProvider {
    override val name: String
        get() = "Fifa2018"
    override val description: String
        get() = "Fifa 2018 World Cup"

    @Autowired
    @Qualifier("FileJsonLoader")
    private lateinit var remote: JsonLoader

    private val data: Lsv by lazy {
        loadData()
    }

    private fun loadData(): Lsv {
        return remote.fetch("https://raw.githubusercontent.com/lsv/fifa-worldcup-2018/master/data.json", Lsv::class.java)
    }

    companion object {
        const val REGULAR_GAMES = 48
        const val PLAYOFF_GAMES = 16
    }

    override fun others(): List<Other> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun matches(): List<Match> {
        val data = loadData()
        if (data.teams != null) {
            return (mapGames("Group A", data.groups.a.matches) +
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
        }
        return listOf()
    }


    private fun mapGames(description: String, matches: List<MatchesItem>?): List<Match> {
        if (matches == null) {
            return listOf()
        }
        return matches.mapNotNull { match -> asGame(description, match) }
    }

    private fun asGame(description: String, match: MatchesItem?): Match? {
        if (match != null && data.teams != null) {
            val home = asTeam(match.homeTeam)
            val away = asTeam(match.awayTeam)
            val df = ISO8601DateFormat()
            return Match(id = match.name, home = home, away = away, description = description, date = df.parse(match.date))
        }
        return null
    }

    private fun asTeam(id: String?): Team {
        return data.teams?.filter { it.id.toString() == id }?.map { Team(it.name, it.iso) }?.firstOrNull() ?: Team.UNKNOWN_TEAM
    }
}



