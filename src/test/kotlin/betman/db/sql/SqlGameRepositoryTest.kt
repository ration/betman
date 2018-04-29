package betman.db.sql

import betman.pojos.Game
import betman.pojos.Match
import betman.pojos.Team
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.*

class SqlGameRepositoryTest : DbTest() {
    private val repo = SqlGameRepository()
    private val germany = Team(1, "Germany", "de")
    private val england = Team(2, "England", "en")
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
        assertEquals(germany, ans.matches[0].home)
        assertEquals(germany, db!!.matches[0].home)
        // TODO Other games, dates
    }

    @Test
    fun update() {

    }

    private fun matches(): List<Match> {
        return mutableListOf(Match(id = 1, home = germany, away = england, description = "round1", date = Date()),
                Match(id = 2, home = england, away = germany, description = "round1", date = Date()))

    }

    private fun createTeams(gameDao: GameDao, vararg teams: Team) {
        for (team in teams) {
            transaction {
                TeamDao.new {
                    name = team.name
                    iso = team.iso
                    externalId = team.id
                }
            }
        }
    }


}