package betman

import org.apache.commons.math3.distribution.GammaDistribution


/**
 * Random score generator for Football scores
 */
object FootballGuesser {

    fun guess(odds: Double?): Int {
        val base = odds ?: 1.0
        val shape = 2.0 - (1 - 1 / base)
        val scale = 1.2
        val gamma = GammaDistribution(shape, scale)
        return gamma.sample().toInt()
    }

}
