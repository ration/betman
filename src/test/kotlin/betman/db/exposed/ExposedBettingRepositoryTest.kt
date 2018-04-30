package betman.db.exposed

import org.junit.Assert
import org.junit.Test

class ExposedBettingRepositoryTest : DbTest() {

    private val repository = ExposedBettingRepository()

    @Test
    fun odds() {
        val odds = repository.odds(1)
        Assert.assertTrue(odds.isEmpty())
    }
}