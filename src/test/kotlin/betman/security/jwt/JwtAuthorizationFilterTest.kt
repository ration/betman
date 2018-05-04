package betman.security.jwt

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilterTest {

    @Mock
    private lateinit var request: HttpServletRequest

    @Mock
    private lateinit var response: HttpServletResponse

    @Mock
    private lateinit var manager: AuthenticationManager

    @Mock
    private lateinit var chain: FilterChain

    @Mock
    private lateinit var provider: JwtTokenProvider

    @Mock
    lateinit var context: SecurityContext

    @Mock
    lateinit var authentication: Authentication


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        whenever(request.pathInfo).thenReturn("/")

    }


    @Test
    fun noToken() {
        val filter = JwtAuthorizationFilter(manager, provider, context)
        filter.doFilter(request, response, chain)
        verify(chain, times(1)).doFilter(request, response)
    }

    @Test
    fun token() {
        val filter = JwtAuthorizationFilter(manager, provider, context)
        val tokenValue = "token"
        whenever(provider.resolveToken(any())).thenReturn(tokenValue)
        whenever(provider.validateToken(any())).thenReturn(true)
        whenever(provider.getAuthentication(eq(tokenValue))).thenReturn(authentication)

        filter.doFilter(request, response, chain)

        verify(context, times(1)).authentication = eq(authentication)
        verify(chain, times(1)).doFilter(request, response)
        verify(provider, times(1)).getAuthentication(eq(tokenValue))
    }
}