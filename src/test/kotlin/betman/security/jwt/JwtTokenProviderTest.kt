package betman.security.jwt

import betman.InvalidTokenException
import betman.config.Settings
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.jsonwebtoken.MalformedJwtException
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import javax.servlet.http.HttpServletRequest

class JwtTokenProviderTest {

    private val settings = Settings()

    @Mock
    private lateinit var request: HttpServletRequest

    @Mock
    private lateinit var userDetailsService: UserDetailsService

    private lateinit var provider: JwtTokenProvider

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        provider = JwtTokenProvider(settings, userDetailsService)
    }

    @Test
    fun createToken() {
        val token = provider.createToken("jack", listOf("USER"))
        assertNotNull(token)
    }

    @Test
    fun resolveTokenNotFound() {
        val token = provider.resolveToken(request)
        assertNull(token)
    }

    @Test
    fun invalidTokenHeader() {
        val tokenValue = "some"
        whenever(request.getHeader(eq("Authorization"))).thenReturn(tokenValue)
        val token = provider.resolveToken(request)
        assertNull(token)
    }

    @Test
    fun resolveTokenFound() {
        val tokenValue = "Bearer tokenValue"
        whenever(request.getHeader(eq("Authorization"))).thenReturn(tokenValue)
        val token = provider.resolveToken(request)
        assertEquals("tokenValue", token)
    }


    @Test
    fun getAuthentication() {
        val details: UserDetails = mock()
        val token = provider.createToken("jack", listOf("USER"))
        whenever(userDetailsService.loadUserByUsername(any())).thenReturn(details)
        val auth = provider.getAuthentication(token)
        assertNotNull(auth)
    }

    @Test(expected = MalformedJwtException::class)
    fun invalidToken() {
        val token = "invalid"
        val details: UserDetails = mock()
        whenever(userDetailsService.loadUserByUsername(any())).thenReturn(details)
        provider.getAuthentication(token)
    }

    @Test
    fun getUsername() {
        val token = provider.createToken("jack", listOf("USER"))
        assertEquals("jack", provider.getUsername(token))
    }

    @Test
    fun validateToken() {
        val token = provider.createToken("jack", listOf("USER"))
        assertTrue(provider.validateToken(token))
    }

    @Test(expected = InvalidTokenException::class)
    fun validateInvalidToken() {
        assertTrue(provider.validateToken("sflkjsdhfölksdjf sdl"))
    }
}