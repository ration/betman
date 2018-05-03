package betman.db.exposed

import betman.UnknownGameException
import betman.UnknownMatchException
import betman.UnknownUserException
import betman.pojos.Bet
import betman.pojos.ScoreBet
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Instant

class ExposedBettingRepositoryTest : DbTest() {

    private val repository = ExposedBettingRepository()
    private val name = "user"

    @Before
    fun init() {
        createUser(name)
    }

    @Test(expected = UnknownMatchException::class)
    fun invalidMatchId() {
        createGame()
        val bet = Bet(scores = listOf(ScoreBet(1, 1, 2)))
        repository.bet(1, bet, name)
        repository.get(1, name)
    }

    @Test(expected = UnknownUserException::class)
    fun invalidUserId() {
        createGame()
        val bet = Bet(scores = listOf(ScoreBet(1, 1, 2)))
        repository.bet(1, bet, "invalid")
    }


    @Test(expected = UnknownGameException::class)
    fun invalidGameId() {
        val bet = Bet(scores = listOf(ScoreBet(1, 1, 2)))
        repository.bet(1, bet, name)
    }

    @Test
    fun bet() {
        val game = createGame()
        makeBet(game)
    }

    @Test
    fun getBet() {
        val game = createGame()
        makeBet(game)
        val bet = repository.get(game.id.value, name)
        assertEquals(44, bet.scores[0].home)
    }

    private fun makeBet(game: GameDao) {
        val match: MatchDao = createMatch(game, createTeam(game, "england", 2),
                createTeam(game, "germany", 1), 1)
        val bet = Bet(scores = listOf(ScoreBet(match.externalId, 44, 2)))
        repository.bet(game.id.value, bet, name)
    }

    private fun createTeam(gameDao: GameDao, team: String, ext: Int): TeamDao {
        return transaction {
            TeamDao.new {
                game = gameDao
                name = team
                iso = "xx"
                externalId = ext
            }
        }
    }

    private fun createMatch(gameDao: GameDao, homeDao: TeamDao, awayDao: TeamDao,
                            ext: Int): MatchDao = transaction {
        MatchDao.new {
            game = gameDao
            externalId = ext
            home = homeDao
            away = awayDao
            date = Instant.now().toEpochMilli()
        }
    }


}