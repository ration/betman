package betman.db.exposed

import betman.*
import betman.pojos.Group
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExposedGroupRepositoryTest : DbTest() {
    private val name = "name"
    private val userName = "name"
    private val description = "description"
    private val key = "key"
    private val displayName = "myDisplayName"
    private val repository = ExposedGroupRepository()
    private val group = Group(name = name, key = key, description = description, game = "game", exactScorePoints = 100,
            admin = "user")


    @Before
    fun init() {
        createUser(userName)
    }

    @Test(expected = InvalidRequestException::class)
    fun missingGame() {
        val group = Group(name = name, description = description, game = "game", admin = "user")
        val groups = repository.create(group, key, userName).blockingGet()!!
        assertNotNull(groups.id)
    }

    @Test
    fun game() {
        createGame()
        val groups = createGroup()
        assertNotNull(groups.id)
        assertEquals(100, groups.exactScorePoints)
    }

    private fun createGroup(): Group {
        val groups = repository.create(group, key, userName).blockingGet()!!
        return groups
    }

    @Test(expected = UnknownUserException::class)
    fun joinUnknownUser() {
        repository.join(key, "someuser", "myName")
    }

    @Test(expected = InvalidKeyException::class)
    fun invalidKey() {
        repository.join("key", userName, "myName")
    }

    @Test
    fun join() {
        createGame()
        createGroup()
        val ans = repository.join(key, userName, displayName).blockingGet()
        assertNotNull(ans.userDisplayName)
    }

    @Test
    fun getSingle() {
        join()
        val db = repository.get(key, userName).blockingGet()
        assertEquals(name, db.name)
        assertEquals(displayName, db.userDisplayName)
    }

    @Test
    fun updateDisplayName() {
        join()
        val db = repository.get(key, userName).blockingGet()
        assertEquals(displayName, db.userDisplayName)
        val newName = "myNewname"
        repository.updateDisplayName(key, userName, newName)
        val db2 = repository.get(key, userName).blockingGet()
        assertEquals(newName, db2.userDisplayName)
    }

    @Test
    fun get() {
        join()
        val db = repository.get(name).blockingGet()
        assertEquals(name, db[0].name)
        assertEquals(displayName, db[0].userDisplayName)
    }


    @Test
    fun updateGroup() {
        join()
        assertEquals(1, group.teamGoalPoints)
        val newGroup = Group(name = "new name", key = key, description = "some", teamGoalPoints = 33, game = "game", admin = "user",
                correctWinnerPoints = 100)
        repository.update(newGroup, userName)
        val saved = repository.get(group.key!!, userName).blockingGet()
        assertEquals(33, saved.teamGoalPoints)
        assertEquals(100, saved.correctWinnerPoints)

    }

    @Test
    fun onlyOwnerCanUpdate() {
        join()
        createUser("another")
        val ans = repository.update(group, "another").blockingGet()
        assertEquals(InvalidUserException::class, ans::class)
    }

    @Test
    fun standings() {
        join()
        val ans = repository.get(key, userName).blockingGet()
        assertEquals(displayName, ans.standings.get(0).displayName)
        assertEquals(0, ans.standings.get(0).points)
    }

    @Test
    fun standingsAreCalculated() {
        join()
        val ans = repository.get(key, userName).blockingGet()
        assertTrue(ans.standings.size == 1)
    }

    @Test(expected = UserAlreadyTakenException::class)
    fun noTwoUsersSameDisplayName() {
        createGame()
        createGroup()
        createUser("another")
        repository.join(key, userName, displayName).blockingGet()
        repository.join(key, "another", displayName).blockingGet()
    }

    @Test(expected = UserAlreadyTakenException::class)
    fun changeNameToSame() {
        createGame()
        createGroup()
        createUser("another")
        repository.join(key, userName, displayName).blockingGet()
        repository.join(key, "another", "someName").blockingGet()
        repository.updateDisplayName(key, "another", displayName)
    }

}