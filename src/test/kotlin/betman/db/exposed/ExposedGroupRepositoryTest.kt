package betman.db.exposed

import betman.InvalidKeyException
import betman.InvalidRequestException
import betman.UnknownUserException
import betman.pojos.Group
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ExposedGroupRepositoryTest : DbTest() {
    private val name = "name"
    private val userName = "name"
    private val description = "description"
    private val key = "key"
    private val displayName = "myDisplayName"
    private val repository = ExposedGroupRepository()


    @Test(expected = InvalidRequestException::class)
    fun missingGame() {
        val group = Group(name = name, description = description, game = "game")
        val groups = repository.create(group, key).blockingGet()!!
        assertNotNull(groups.id)
    }

    @Test
    fun game() {
        createGame()
        val group = Group(name = name, description = description, game = "game")
        val groups = repository.create(group, key).blockingGet()!!
        assertNotNull(groups.id)
    }

    @Test(expected = UnknownUserException::class)
    fun joinUnknownUser() {
        repository.join("key", userName, "myName")
    }

    @Test(expected = InvalidKeyException::class)
    fun invalidKey() {
        createUser(userName)
        repository.join("key", userName, "myName")
    }

    @Test
    fun join() {
        createUser(userName)
        createGame()
        val group = Group(name = name, description = description, game = "game")
        repository.create(group, key)
        val ans = repository.join(key, userName, displayName).blockingGet()
        assertNotNull(ans.userDisplayName)
    }

    @Test
    fun getSingle() {
        join()
        val db = repository.get(name, userName).blockingGet()
        assertEquals(name, db.name)
        assertEquals(displayName, db.userDisplayName)
    }

    @Test
    fun update() {
        join()
        val db = repository.get(name, userName).blockingGet()
        assertEquals(displayName, db.userDisplayName)
        val newName = "myNewname"
        repository.updateDisplayName(name, userName, newName)
        val db2 = repository.get(name, userName).blockingGet()
        assertEquals(newName, db2.userDisplayName)
    }

    @Test
    fun get() {
        join()
        val db = repository.get(name).blockingGet()
        assertEquals(name, db[0].name)
        assertEquals(displayName, db[0].userDisplayName)
    }


}