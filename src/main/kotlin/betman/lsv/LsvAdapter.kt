package betman.lsv

import betman.pojos.Game
import betman.pojos.Team
import com.fasterxml.jackson.databind.util.ISO8601DateFormat
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

/**
 * Adapter for LSV provided WorldCup data as one JSON response
 */
class LsvAdapter {

    val data = fetchRemote()

    companion object {
        const val REGULAR_GAMES = 48
        const val PLAYOFF_GAMES = 16
    }

    fun fetchRemote(): Lsv {
        val remoteUrl = "https://raw.githubusercontent.com/lsv/fifa-worldcup-2018/master/data.json"
        val template = RestTemplate()
        template.messageConverters.filterIsInstance<MappingJackson2HttpMessageConverter>().forEach { it.supportedMediaTypes = listOf(MediaType.TEXT_PLAIN) }
        return template.getForObject(remoteUrl, Lsv::class.java)
    }

    fun regularGames(): List<Game> {
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


    private fun mapGames(description: String, matches: List<MatchesItem>?): List<Game> {
        if (matches == null) {
            return listOf()
        }
        return matches.mapNotNull { match -> asGame(description, match) }
    }

    private fun asGame(description: String, match: MatchesItem?): Game? {
        if (match != null && data.teams != null) {
            val home = asTeam(match.homeTeam)
            val away = asTeam(match.awayTeam)
            val df = ISO8601DateFormat()
            return Game(match.name, home, away, description, df.parse(match.date))
        }
        return null
    }

    private fun asTeam(id: String?): Team {
        return data.teams?.filter { it.id.toString() == id }?.map { Team(it.name, it.iso) }?.firstOrNull() ?: Team.UNKNOWN_TEAM
    }
}



