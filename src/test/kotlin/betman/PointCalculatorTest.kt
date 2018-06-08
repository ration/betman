package betman

import betman.pojos.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.stereotype.Component
import java.util.*

@Component
class PointCalculatorTest {
    private final val team1 = Team(name = "Finland", id = 0, iso = "fi")
    private final val team2 = Team(name = "Sweden", id = 1, iso = "se")
    private final val match1 = Match(id = 1, home = team1.id, away = team2.id, homeGoals = 5, awayGoals = 0, description = "1", date = Date())
    private val match2 = Match(id = 2, home = team2.id, away = team1.id, homeGoals = 3, awayGoals = 3, description = "2", date = Date())
    private val game = Game(name = "game", description = "description", matches = listOf(match1, match2))
    private val group = Group(name = "x", game = "game", key = "somekey", admin = "user")


    @Test
    fun noBets() {
        val user1Bet = Bet(groupKey = group.key!!)
        val score: Int = PointCalculator.calculate(group, game, null, null, user1Bet)
        assertEquals(0, score)
    }

    @Test
    fun correctHome() {
        val user1Bet = Bet(groupKey = group.key!!, scores = listOf(ScoreBet(1, home = 5, away = 5)))
        val score: Int = PointCalculator.calculate(group, game, null, null, user1Bet)
        assertEquals(group.teamGoalPoints, score)
    }

    @Test
    fun correctAway() {
        val user1Bet = Bet(groupKey = group.key!!, scores = listOf(ScoreBet(1, home = 0, away = 0)))
        val score: Int = PointCalculator.calculate(group, game, null, null, user1Bet)
        assertEquals(group.teamGoalPoints, score)
    }

    @Test
    fun exact() {
        val user1Bet = Bet(groupKey = group.key!!, scores = listOf(ScoreBet(1, home = 5, away = 0)))
        val score: Int = PointCalculator.calculate(group, game, null, null, user1Bet)
        assertEquals(group.exactScorePoints, score)
    }

    @Test
    fun exactAndSingle() {
        val user1Bet = Bet(groupKey = group.key!!, scores = listOf(ScoreBet(1, home = 5, away = 0),
                ScoreBet(2, home = 3, away = 2)))
        val score: Int = PointCalculator.calculate(group, game, null, null, user1Bet)
        assertEquals(group.exactScorePoints + group.teamGoalPoints, score)
    }

    @Test
    fun winner() {
        val bet = Bet(groupKey = group.key!!, scores = listOf(), winner = team1.id)
        val score: Int = PointCalculator.calculate(group, game, team1.id, null, bet)
        assertEquals(group.winnerPoints, score)
    }

    @Test
    fun goalKing() {
        val bet = Bet(groupKey = group.key!!, scores = listOf(), winner = team1.id, goalKing = "Some King")
        val score: Int = PointCalculator.calculate(group, game, null, "Some King", bet)
        assertEquals(group.goalKingPoints, score)
    }

    @Test
    fun matchWinner() {
        val bet = Bet(groupKey = group.key!!, scores = listOf(ScoreBet(1, home = 3, away = 1)))
        val score: Int = PointCalculator.calculate(group, game, null, "XXXX", bet)
        assertEquals(group.correctWinnerPoints, score)
    }

    @Test
    fun matchWinnerAndSingle() {
        val bet = Bet(groupKey = group.key!!, scores = listOf(ScoreBet(1, home = 5, away = 1)))
        val score: Int = PointCalculator.calculate(group, game, null, "XXXX", bet)
        assertEquals(group.correctWinnerPoints + group.teamGoalPoints, score)
    }
}