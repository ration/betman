package betman.controller

import betman.db.UserRepository
import betman.pojos.User
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.security.Principal

class UserControllerTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userController: UserController

    private val user = User(name = "name")

    @Mock
    private lateinit var principal: Principal

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun register() {
        val ans = User(id = 1, name = "name")
        whenever(userRepository.register(user)).thenReturn(Observable.just(ans).singleOrError())
        userController.register(user)
        verify(userRepository, times(1)).register(any())
    }

    @Test
    fun status() {
        val username = "name"
        whenever(principal.name).thenReturn(username)

        whenever(userRepository.get(eq("name"))).thenReturn(Maybe.just(user))

        userController.status(principal)

        verify(userRepository, times(1)).get(eq(username))

    }

}