package betman.db.exposed

import betman.db.InvalidKeyException
import betman.db.InvalidRequestException
import betman.db.UnknownUserException
import betman.pojos.Group
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertNotNull
import org.junit.Test

class ExposedGroupRepositoryTest : DbTest() {
    private val name = "name"
    private val userName = "name"
    private val description = "description"
    private val key = "key"
    private val repository = ExposedGroupRepository()


    @Test(expected = InvalidRequestException::class)
    fun missingGame() {
        val group = Group(name = name, description = description, game = 1)
        val groups = repository.create(group, key)
        assertNotNull(groups.id)
    }

    @Test
    fun game() {
        val game: GameDao = createGame()
        val group = Group(name = name, description = description, game = game.id.value)
        val groups = repository.create(group, key)
        assertNotNull(groups.id)
    }

    @Test(expected = UnknownUserException::class)
    fun joinUnknownUser() {
        repository.join("key", "myName", userName)
    }

    @Test(expected = InvalidKeyException::class)
    fun invalidKey() {
        createUser()
        repository.join("key", "myName", userName)
    }

    @Test
    fun join() {
        createUser()
        val game: GameDao = createGame()
        val group = Group(name = name, description = description, game = game.id.value)
        val groups = repository.create(group, key)
        val ans = repository.join(key, "myDisplayName", userName)
        assertNotNull(ans.userDisplayName)

    }

    private fun createGame(): GameDao = transaction {
        GameDao.new {
            name = "test"
            description = "description"
        }
    }

    private fun createUser() = transaction {
        UserDao.new {
            name = userName
            password = "password"
        }
    }

}