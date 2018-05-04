package betman.db.exposed

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
        val bet = Bet("game", scores = listOf(ScoreBet(1, 1, 2)))
        repository.bet("game", bet, name)
        repository.get("game", name)
    }

    @Test(expected = UnknownUserException::class)
    fun invalidUserId() {
        createGame()
        val bet = Bet("game", scores = listOf(ScoreBet(1, 1, 2)))
        repository.bet("game", bet, "invalid")
    }


    @Test(expected = UnknownMatchException::class)
    fun invalidGameId() {
        val bet = Bet("game", scores = listOf(ScoreBet(1, 1, 2)))
        repository.bet("game", bet, name)
    }

    @Test
    fun bet() {
        val game = createGame()
        makeBet(game)
    }

    @Test
    fun update() {
        val game = createGame()
        val match = makeBet(game)
        val bet = Bet("game", scores = listOf(ScoreBet(match.externalId, 56, 2)))
        repository.bet("game", bet, name)
        val update = repository.get("game", name).blockingGet()
        assertEquals(56, update.scores[0].home)
    }


    @Test
    fun getBet() {
        val game = createGame()
        makeBet(game)
        val bet = repository.get("game", name).blockingGet()
        assertEquals(44, bet.scores[0].home)
    }

    private fun makeBet(game: GameDao): MatchDao {
        val match: MatchDao = createMatch(game, createTeam(game, "england", 2),
                createTeam(game, "germany", 1), 1)
        val bet = Bet("game", scores = listOf(ScoreBet(match.externalId, 44, 2)))
        repository.bet("game", bet, name)
        return match
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