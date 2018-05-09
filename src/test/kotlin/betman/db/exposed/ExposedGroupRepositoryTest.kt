package betman.db.exposed

import betman.InvalidKeyException
import betman.InvalidRequestException
import betman.InvalidUserException
import betman.UnknownUserException
import betman.pojos.Group
import org.junit.Assert.*
import org.junit.Test

class ExposedGroupRepositoryTest : DbTest() {
    private val name = "name"
    private val userName = "name"
    private val description = "description"
    private val key = "key"
    private val displayName = "myDisplayName"
    private val repository = ExposedGroupRepository()
    val group = Group(name = name, description = description, game = "game", exactScorePoints = 100)


    @Test(expected = InvalidRequestException::class)
    fun missingGame() {
        val group = Group(name = name, description = description, game = "game")
        val groups = repository.create(group, key).blockingGet()!!
        assertNotNull(groups.id)
    }

    @Test
    fun game() {
        val groups = createGroup()
        assertNotNull(groups.id)
        assertEquals(100, groups.exactScorePoints)
    }

    private fun createGroup(): Group {
        createGame()
        val groups = repository.create(group, key).blockingGet()!!
        return groups
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
        createGroup()
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

    @Test(expected = InvalidUserException::class)
    fun getGroupWhenUserNotInGroup() {
        fail()
    }

    @Test
    fun updateGroup() {
        val group = createGroup()
        assertEquals(1, group.teamGoalPoints)
        val newGroup = Group(name = name, description = description, game = "game", teamGoalPoints = 33)

        repository.update(newGroup, userName)
        val saved = repository.get(newGroup.name, userName).blockingGet()
        assertEquals(33, saved.teamGoalPoints)
    }

    @Test
    fun onlyOwnerCanUpdate() {
        fail()
    }

}