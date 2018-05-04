package betman.controller

import betman.db.UserRepository
import betman.pojos.User
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.security.core.userdetails.UserDetailsService

class UserControllerTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var userDetailsService: UserDetailsService

    @InjectMocks
    lateinit var userController: UserController

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun register() {
        val user = User(name = "name")
        val ans = User(id = 1, name = "name")
        whenever(userRepository.register(user)).thenReturn(Observable.just(ans).singleOrError())
        userController.register(user)
        verify(userRepository, times(1)).register(any())
    }


    fun login() {
        val user = User(name = "name")
        //val ans: Single<User> = userController.login(user)
        fail()
    }


}