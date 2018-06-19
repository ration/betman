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
    val bet = Bet(key, scores = listOf(ScoreBet(1, 1, 2)))


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
        repository.bet(key, bet, name)
        repository.get(key, name)
    }

    @Test(expected = UnknownUserException::class)
    fun invalidUserId() {
        repository.bet(key, bet, "invalid")
    }


    @Test(expected = UnknownMatchException::class)
    fun invalidGameId() {
        repository.bet(key, bet, name)
    }

    @Test
    fun bet() {
        makeBet(game)
    }

    @Test
    fun update() {
        val match = makeBet(game)
        assertEquals("Jack", repository.get(group.key!!, name).blockingGet().goalKing)
        val bet = Bet(key, scores = listOf(ScoreBet(match.externalId, 56, 2)),
                goalKing = "John", winner = 2)
        repository.bet(key, bet, name)
        val update = repository.get(key, name).blockingGet()
        assertEquals(56, update.scores[0].home)
        assertEquals("John", update.goalKing)
        assertEquals(2, update.winner)
        assertEquals(group.key, update.groupKey)

    }

    @Test
    fun multipleGames() {
        makeBet(game)
        val game2 = createGame("other")
        val germany = createTeam(game2, "germany", 1)
        val england = createTeam(game2, "england", 2)
        val match: MatchDao = createMatch(game2, germany, england, 2)
        val match2: MatchDao = createMatch(game2, england, germany, 3)
        val bet = Bet(key, scores = listOf(ScoreBet(match.externalId, 44, 2),
                ScoreBet(match2.externalId, 22, 21)),
                winner = germany.externalId, goalKing = "Jack")
        repository.bet(key, bet, name)

    }


    @Test
    fun getBet() {
        makeBet(game)
        val bet = repository.get(key, name).blockingGet()
        assertEquals(2, bet.scores.size)
        assertEquals(44, bet.scores[0].home)
        assertEquals(1, bet.winner)
    }

    @Test
    fun cantBetOnPast() {
        makeBet(game)
        var bet = repository.get(key, name).blockingGet()
        bet.scores[0].home = 10
        bet = repository.bet(key, bet, name).blockingGet()
        assertEquals(10, bet.scores[0].home)
        setGameToPast(2)
        bet.scores[0].home = 1
        bet = repository.bet(key, bet, name).blockingGet()
        assertEquals(10, bet.scores[0].home)
    }

    private fun setGameToPast(id: Int) {
        transaction {
            val match = MatchDao.find { Matches.externalId eq id }.single()
            match.date = Instant.now().minusSeconds(1000).toEpochMilli()
        }
    }

    private fun makeBet(game: GameDao): MatchDao {
        val germany = createTeam(game, "germany", 1)
        val england = createTeam(game, "england", 2)
        val match: MatchDao = createMatch(game, germany, england, 2)
        val match2: MatchDao = createMatch(game, england, germany, 3)
        val bet = Bet(key, scores = listOf(ScoreBet(match.externalId, 44, 2),
                ScoreBet(match2.externalId, 22, 21)),
                winner = germany.externalId, goalKing = "Jack")
        repository.bet(key, bet, name)
        return match
    }


}