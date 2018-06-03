package betman.db.exposed

import betman.InvalidRequestException
import betman.UserAlreadyTakenException
import betman.pojos.User
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.security.crypto.password.PasswordEncoder

class ExposedUserRepositoryTest : DbTest() {

    @Mock
    lateinit var encoder: PasswordEncoder

    @InjectMocks
    lateinit var repo: ExposedUserRepository

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        whenever(encoder.encode(any())).thenReturn("secret")
    }

    @Test
    fun register() {
        val user = User(id = null, name = "testUser", password = "myPassword")
        val dbUser = repo.register(user).blockingGet()
        assertNotNull(dbUser.id)
    }

    @Test(expected = InvalidRequestException::class)
    fun noPassword() {
        repo.register(User(name = "test"))
    }

    @Test(expected = UserAlreadyTakenException::class)
    fun usernameTaken() {
        val user = User(id = null, name = "testUser", password = "myPassword")
        repo.register(user)
        repo.register(user)
    }

    @Test
    fun get() {
        val user = User(id = null, name = "testUser", password = "myPassword")
        repo.register(user)
        val ans = repo.get(user.name).blockingGet()
        assertEquals(user.name, ans?.name)
        assertEquals("secret", ans?.password)
    }

    @Test()
    fun noUser() {
        repo.get("some").subscribe({
            Assert.fail()
        }, { Assert.fail() }, {})

    }

    @Test
    fun multipleUsers() {
        for (i in 1..100) {
            val user = User(name = "user$i", password = "some")
            val fromDb: Maybe<User> = repo.register(user).map { repo.get(it.name) }.blockingGet()
            assertEquals("user$i", fromDb.blockingGet().name)
        }
    }

}