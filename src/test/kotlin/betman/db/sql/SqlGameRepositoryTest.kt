package betman.db.sql

import betman.pojos.Game
import org.junit.Assert.*
import org.junit.Test

class SqlGameRepositoryTest : DbTest() {
    @Test
    fun create() {
        val game = Game(name = "test", description = "description")
        val repo = SqlGameRepository()
        val ans = repo.create(game)
        assertNotNull(ans.id)
    }
}