package betman.db.exposed

import betman.pojos.Game
import betman.pojos.Match
import betman.pojos.Team
import org.junit.Assert.*
import org.junit.Test
import java.time.Instant
import java.util.*

class ExposedGameRepositoryTest : DbTest() {
    private val repo = ExposedGameRepository()
    private val germany = Team(1, "Germany", "de")
    private val england = Team(2, "England", "en")
    private val game = Game(name = "test", description = "description",
            matches = matches(), teams = listOf(germany, england))


    @Test
    fun create() {
        val ans = repo.create(game).blockingGet()
        assertNotNull(ans.id)
    }

    @Test
    fun get() {
        val ans = repo.create(game).blockingGet()
        val db = repo.get(ans.name).blockingGet()

        assertNotNull(db)
        assertEquals(germany.id, ans.matches[0].home)
        assertEquals(1, ans.matches[0].homeGoals)
        assertEquals(germany.id, db!!.matches[0].home)
        assertNotNull(ans.matches[0].date)
        assertTrue(db.teams.contains(germany))
        assertEquals(2, db.teams.size)
        assertEquals(db.matches[0].homeOdds, matches()[0].homeOdds)

    }

    @Test
    fun games() {
        val game2 = Game(name = "test2", description = "description",
                matches = matches())
        repo.create(game)
        repo.create(game2)
        assertEquals(listOf("test", "test2"), repo.games().blockingGet())
    }

    @Test
    fun update() {
        repo.create(game)
        val description = "new description"
        val game2 = Game(name = game.name, description = description,
                matches = matches2())
        val updated = repo.update(game2).blockingGet()
        val db = repo.get(game2.name).blockingGet()!!
        assertNotNull(db)
        assertEquals(description, updated?.description)
        assertEquals(description, db.description)
        assertEquals(3, db.matches[0].homeGoals)

    }

    private fun matches(): List<Match> {
        return listOf(Match(id = 1, home = germany.id, away = england.id, description = "round1", date = Date.from(Instant.now()),
                homeGoals = 1, awayGoals = 2, homeOdds = 1.2, awayOdds = 2.3),
                Match(id = 2, home = england.id, away = germany.id, description = "round1", date = Date.from(Instant.now())))

    }

    private fun matches2(): List<Match> {
        return listOf(Match(id = 1, home = germany.id, away = england.id, description = "round1", date = Date.from(Instant.now()),
                homeGoals = 3, awayGoals = 4),
                Match(id = 2, home = england.id, away = germany.id, description = "round1", date = Date.from(Instant.now())))

    }

}