package betman.security


import betman.db.UserRepository
import betman.pojos.User
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.security.core.userdetails.UsernameNotFoundException

class DbUserDetailsServiceTest {

    @Mock
    lateinit var userRepository: UserRepository


    @InjectMocks
    lateinit var service: DbUserDetailsService

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun loadUserByUsername() {
        whenever(userRepository.get(any())).thenReturn(Maybe.just(User(name = "test", password = "password")))
        service.loadUserByUsername("test")
        verify(userRepository, times(1)).get(any())
    }

    @Test(expected = UsernameNotFoundException::class)
    fun notFound() {
        whenever(userRepository.get(any())).thenReturn(Maybe.empty())
        service.loadUserByUsername("test")
        verify(userRepository, times(1)).get(any())
    }


}
