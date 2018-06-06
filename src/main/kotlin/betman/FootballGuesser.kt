package betman

import org.apache.commons.math3.distribution.EnumeratedDistribution
import org.apache.commons.math3.distribution.PoissonDistribution
import java.lang.Math.exp
import java.lang.Math.pow


/**
 * Random score generator for Football scores
 */
object FootballGuesser {
    
    fun guess(odds: Double?): Int {
        // We don't have the actual goal stats that poisson works best. Just do some guesstimation
        val base = 1-(1-1/(odds ?: 1.0)) + 0.5
        val distribution = (0..7).map { org.apache.commons.math3.util.Pair(it,poisson(it,base)) }
        val engine = EnumeratedDistribution(distribution)
        return engine.sample()
    }

    private fun factorial(n: Int): Double {
        var fact = 1
        for (i in 1..n) fact *= i
        return fact.toDouble()
    }

    fun poisson(r: Int, mean: Double): Double {
        return exp(-1 * mean) * pow(mean, r.toDouble()) / factorial(r)
    }

}
