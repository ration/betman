package betman.security.jwt

import betman.pojos.User
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.mock.web.DelegatingServletInputStream
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import java.io.ByteArrayInputStream
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthenticationFilterTest {

    @Mock
    lateinit var manager: AuthenticationManager

    @Mock
    lateinit var tokenProvider: JwtTokenProvider

    @Mock
    lateinit var request: HttpServletRequest


    @Mock
    lateinit var response: HttpServletResponse

    @Mock
    lateinit var authentication: Authentication

    @Mock
    lateinit var chain: FilterChain

    private val user = User(name = "test", password = "password")

    private lateinit var filter: JwtAuthenticationFilter

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        filter = JwtAuthenticationFilter(manager, tokenProvider)
    }

    @Test
    fun attemptAuthentication() {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val encoded = mapper.writeValueAsBytes(user)
        val stream = ByteArrayInputStream(encoded)
        whenever(request.inputStream).thenReturn(DelegatingServletInputStream(stream))
        whenever(manager.authenticate(any())).thenReturn(authentication)
        val auth = filter.attemptAuthentication(request, response)
        verify(manager, times(1)).authenticate(any())
        assertNotNull(auth)
    }

}