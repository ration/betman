package betman.security.jwt

import betman.InvalidTokenException
import betman.config.Settings
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.springframework.security.core.userdetails.UserDetailsService

class JwtTokenProviderTest {

    val settings = Settings()

    @Mock
    lateinit var userDetailsService: UserDetailsService

    lateinit var provider: JwtTokenProvider

    @Before
    fun init() {
        provider = JwtTokenProvider(settings, userDetailsService)
    }

    @Test
    fun createToken() {
        val token = provider.createToken("jack", listOf("USER"))
        assertNotNull(token)
    }

    @Test
    fun resolveToken() {
        fail()
    }

    @Test
    fun resolveTokenAsNull() {
        fail()
    }

    @Test
    fun getAuthentication() {
        fail()
    }

    @Test
    fun getUsername() {
        fail()
    }

    @Test
    fun validateToken() {
        fail()
    }

    @Test(expected = InvalidTokenException::class)
    fun validateInvalidToken() {
        fail()
    }
}