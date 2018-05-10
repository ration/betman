package betman.db.exposed

import betman.UnknownMatchException
import betman.UnknownUserException
import betman.pojos.Bet
import betman.pojos.Group
import betman.pojos.ScoreBet
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Instant

class ExposedBettingRepositoryTest : DbTest() {

    private val repository = ExposedBettingRepository()
    private val name = "user"
    private val key = "mykey"
    private lateinit var game: GameDao
    private lateinit var group: Group

    @Before
    fun init() {
        createUser(name)
        game = createGame()
        group = createGroup("newGroup", key, game, name).blockingGet()
        val groupRepo = ExposedGroupRepository()
        groupRepo.join(group.key!!, name, "displayName")
    }

    @Test(expected = UnknownMatchException::class)
    fun invalidMatchId() {
        val bet = Bet(key, scores = listOf(ScoreBet(1, 1, 2)))
        repository.bet(key, bet, name)
        repository.get(key, name)
    }

    @Test(expected = UnknownUserException::class)
    fun invalidUserId() {
        val bet = Bet(key, scores = listOf(ScoreBet(1, 1, 2)))
        repository.bet(key, bet, "invalid")
    }


    @Test(expected = UnknownMatchException::class)
    fun invalidGameId() {
        val bet = Bet(key, scores = listOf(ScoreBet(1, 1, 2)))
        repository.bet(key, bet, name)
    }

    @Test
    fun bet() {
        makeBet(game)
    }

    @Test
    fun update() {
        val match = makeBet(game)
        val bet = Bet(key, scores = listOf(ScoreBet(match.externalId, 56, 2)))
        repository.bet(key, bet, name)
        val update = repository.get(key, name).blockingGet()
        assertEquals(56, update.scores[0].home)
    }


    @Test
    fun getBet() {
        makeBet(game)
        val bet = repository.get(key, name).blockingGet()
        assertEquals(44, bet.scores[0].home)
    }

    private fun makeBet(game: GameDao): MatchDao {
        val match: MatchDao = createMatch(game, createTeam(game, "england", 2),
                createTeam(game, "germany", 1), 1)
        val bet = Bet(key, scores = listOf(ScoreBet(match.externalId, 44, 2)))
        repository.bet(key, bet, name)
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