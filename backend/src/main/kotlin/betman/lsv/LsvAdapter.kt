package betman.lsv

import betman.pojos.Game
import betman.pojos.Team
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import com.fasterxml.jackson.databind.util.ISO8601DateFormat


class LsvAdapter {

    private val data = fetchRemote()

    companion object {
        const val REGULAR_GAMES = 48


    }

    fun fetchRemote(): Lsv {
        val remoteUrl = "https://raw.githubusercontent.com/lsv/fifa-worldcup-2018/master/data.json"
        val template = RestTemplate()
        template.messageConverters.filterIsInstance<MappingJackson2HttpMessageConverter>().forEach { it.supportedMediaTypes = listOf(MediaType.TEXT_PLAIN) }
        return template.getForObject(remoteUrl, Lsv::class.java)
    }

    fun regularGames(): List<Game> {
        if (data.teams != null) {
            return union(data.groups.a.matches,
                    data.groups.b.matches,
                    data.groups.c.matches,
                    data.groups.d.matches,
                    data.groups.e.matches,
                    data.groups.f.matches,
                    data.groups.g.matches,
                    data.groups.h.matches).map { asGame(it) }.filterNotNull()
        }
        return listOf()
    }


    private fun union(vararg items: List<MatchesItem>?): List<MatchesItem> {
        return items.filterNotNull().flatMap { it }
    }

    private fun asGame(match: MatchesItem?): Game? {
        if (match != null && data.teams != null) {
            val home = asTeam(match.homeTeam)
            val away = asTeam(match.awayTeam)
            if (home != null && away != null) {
                val df = ISO8601DateFormat()

                return Game(match.name, home, away, df.parse(match.date))
            }
        }
        return null
    }

    private fun asTeam(id: String?): Team? {
        return data.teams?.filter { it.id.toString() == id }?.map { Team(it.name) }?.first()
    }
}



