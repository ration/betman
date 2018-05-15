package betman

import betman.pojos.Bet
import betman.pojos.Game
import betman.pojos.Group
import betman.pojos.Match


object PointCalculator {


    fun calculate(group: Group,
                  game: Game,
                  winnerId: Int?,
                  goalKing: String?,
                  bet: Bet): Int {
        return game.matches.map { toPoints(it, bet, group) }.sum() +
                winnerPoints(group, bet, winnerId) +
                goalKing(group, bet, goalKing)
    }

    private fun toPoints(match: Match, bet: Bet, group: Group): Int {
        if (match.homeGoals != null) {
            if (exactScoreMatch(bet, match)) {
                return group.exactScorePoints
            } else if (oneTeamScoreMatch(bet, match)) {
                return group.teamGoalPoints
            }
        }
        return 0
    }

    private fun winnerPoints(group: Group, bet: Bet, winnerId: Int?): Int = if (winnerId != null &&
            bet.winner == winnerId) group.winnerPoints else 0

    private fun goalKing(group: Group, bet: Bet, goalKing: String?): Int = if (goalKing != null &&
            bet.goalKing == goalKing) group.goalKingPoints else 0

    private fun oneTeamScoreMatch(bet: Bet, match: Match) =
            bet.scores[match.id]?.home == match.homeGoals ||
                    bet.scores[match.id]?.away == match.awayGoals

    private fun exactScoreMatch(bet: Bet, match: Match) =
            bet.scores[match.id]?.home == match.homeGoals &&
                    bet.scores[match.id]?.away == match.awayGoals
}

