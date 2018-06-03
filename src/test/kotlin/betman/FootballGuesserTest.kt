package betman

import org.junit.Assert.assertTrue
import org.junit.Test

class FootballGuesserTest {
    @Test
    fun guess() {
        val results = mutableListOf<Int>()
        val results2 = mutableListOf<Int>()

        for (i in 1..100) {
            results += FootballGuesser.guess(1.2)
            results2 += FootballGuesser.guess(10.2)

        }
        println("results = ${results}")
        println("results = ${results2}")

        assertTrue(results.filter { it == 0 }.size < results2.filter { it == 0 }.size) // This can fail with some small probability if the
    }

}