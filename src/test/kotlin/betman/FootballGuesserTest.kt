package betman

import junit.framework.Assert.assertTrue
import org.junit.Test

enum class Winner {
    HOME, AWAY, TIE
}


class FootballGuesserTest {

    @Test
    fun guess() {
        val results = mutableListOf<Pair<Int, Int>>()
        val results2 = mutableListOf<Pair<Int, Int>>()

        val results3 = mutableListOf<Pair<Int, Int>>()

        for (i in 1..1000) {
            results += Pair(FootballGuesser.guess(1.2), FootballGuesser.guess(10.2))
            results2 += Pair(FootballGuesser.guess(1.5), FootballGuesser.guess(3.2))
            results3 += Pair(FootballGuesser.guess(4.06), FootballGuesser.guess(2.00))

        }
        printStats(1.2, 10.2, results)
        printStats(1.5, 3.2, results2)
        printStats(4.06, 2.00, results3)
        // This still has some probability for failure :(
        assertTrue(results.filter{it.first >= it.second}.size > 900)

    }

    private fun printStats(homeOdds: Double, awayOdds: Double, results: MutableList<Pair<Int, Int>>) {
        //println("results = ${results}")
        val ratio = results.groupBy {
            val winner = it.first - it.second
            when {
                winner == 0 -> Winner.TIE
                winner > 0 -> Winner.HOME
                else -> Winner.AWAY
            }
        }
        val home = (100 * ((ratio[Winner.HOME]?.size?.toDouble() ?: 0.0) / results.size.toDouble())).toInt()
        val away = (100 * ((ratio[Winner.AWAY]?.size?.toDouble() ?: 0.0) / results.size.toDouble())).toInt()
        val tie = (100 * ((ratio[Winner.TIE]?.size?.toDouble() ?: 0.0) / results.size.toDouble())).toInt()
        println("Home $homeOdds Away $awayOdds $home/$tie/$away}")
    }
}

