package betman.db.exposed

import betman.pojos.User
import org.junit.Assert.assertNotNull
import org.junit.Test

class ExposedUserRepositoryTest {

    @Test
    fun register() {
        val repo = ExposedUserRepository()
        val user = User(id = null, name = "testUser", password = "myPassword")
        val dbUser = repo.register(user)
        assertNotNull(dbUser.id)
    }
}