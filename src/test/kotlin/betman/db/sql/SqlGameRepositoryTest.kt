package betman.db.sql

import betman.pojos.Game
import betman.pojos.Match
import betman.pojos.Team
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.*

class SqlGameRepositoryTest : DbTest() {
    private val repo = SqlGameRepository()
    private val germany = Team("Germany", "de")
    private val england = Team("England", "en")
    private val game = Game(name = "test", description = "description",
            matches = matches())



    @Test
    fun create() {
        val ans = repo.create(game)
        assertNotNull(ans.id)
    }

    @Test
    fun get() {
        val ans = repo.create(game)
        val db = repo.get(ans.name)
        assertNotNull(db)
        assertEquals(germany.name, ans.matches[0].home)
    }

    @Test
    fun update() {

    }

    private fun matches(): List<Match> {
        return mutableListOf(Match(id = 1, home = germany, away = england, description = "round1", date = Date()),
                Match(id = 2, home = england, away = germany, description = "round1", date = Date()))

    }


}