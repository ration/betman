package betman.db.sql

import org.junit.Assert
import org.junit.Test

class SqlBettingRepositoryTest : DbTest() {

    private val repository = SqlBettingRepository()

    @Test
    fun odds() {
        val odds = repository.odds(1)
        Assert.assertTrue(odds.isEmpty())
    }
}