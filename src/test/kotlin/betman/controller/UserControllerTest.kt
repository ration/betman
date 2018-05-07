package betman.controller

import betman.db.UserRepository
import betman.pojos.User
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class UserControllerTest {

    @Mock
    lateinit var userRepository: UserRepository

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

}