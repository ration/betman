package betman

import betman.pojos.Bet
import betman.pojos.Game
import betman.pojos.Group
import betman.pojos.Match


object PointCalculator {

    enum class Winner {
        HOME, TIE, AWAY;

        companion object {
            fun fromScore(home: Int, away: Int): Winner {
                val result = home - away
                return when {
                    result > 0 -> HOME
                    result == 0 -> TIE
                    else -> AWAY
                }
            }
        }
    }

    fun calculate(group: Group,
                  game: Game,
                  winnerId: Int?,
                  goalKing: String?,
                  bet: Bet): Int {
        return game.matches.map { toPoints(it, bet, group) }.sum() +
                winnerPoints(group, bet, winnerId) +
                goalKing(group, bet, goalKing)
    }

    /**
     * Accumulation of points per game
     */
    fun pointsPerGame(group: Group, game: Game, bet: Bet): Map<Int, Int> {
        var sum = 0
        val ans: MutableMap<Int, Int> = mutableMapOf()
        for (match in game.matches) {
            if (match.homeGoals == null) {
                continue
            }
            sum += toPoints(match, bet, group)
            ans[match.id] = sum
        }
        return ans}

    private fun toPoints(match: Match, bet: Bet, group: Group): Int {
        if (match.homeGoals != null) {
            return if (exactScoreMatch(bet, match)) {
                group.exactScorePoints
            } else {
                oneTeamScorePoints(bet, match, group) + matchWinnerPoints(bet, match, group)
            }
        }
        return 0
    }

    private fun matchWinnerPoints(bet: Bet, match: Match, group: Group): Int {
        val scoreBet = bet.scores.find { it.matchId == match.id }
        if (scoreBet != null) {
            return (match.homeGoals to match.awayGoals).biLet { homeGoals, awayGoals ->
                val result = Winner.fromScore(homeGoals, awayGoals)
                val betResult = Winner.fromScore(scoreBet.home, scoreBet.away)
                if (result == betResult) group.correctWinnerPoints else 0
            } ?: 0
        }
        return 0
    }

    fun <T, U, R> Pair<T?, U?>.biLet(body: (T, U) -> R): R? {
        return first?.let { f -> second?.let { s -> body(f, s) } }
    }

    private fun winnerPoints(group: Group, bet: Bet, winnerId: Int?): Int = if (winnerId != null &&
            bet.winner == winnerId) group.winnerPoints else 0

    private fun goalKing(group: Group, bet: Bet, goalKing: String?): Int = if (goalKing != null &&
            bet.goalKing == goalKing) group.goalKingPoints else 0

    private fun oneTeamScorePoints(bet: Bet, match: Match, group: Group): Int {
        return if (bet.scores.find { it.matchId == match.id }?.home == match.homeGoals ||
                bet.scores.find { it.matchId == match.id }?.away == match.awayGoals) group.teamGoalPoints else 0
    }

    private fun exactScoreMatch(bet: Bet, match: Match) =
            bet.scores.find { it.matchId == match.id }?.home == match.homeGoals &&
                    bet.scores.find { it.matchId == match.id }?.away == match.awayGoals


}

