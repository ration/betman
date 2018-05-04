package betman.security.jwt

import betman.pojos.User
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthenticationFilter(private val manager: AuthenticationManager,
                              private val tokenProvider: JwtTokenProvider) : UsernamePasswordAuthenticationFilter() {

    private val mapper = ObjectMapper()

    init {
        authenticationManager = manager
        mapper.registerModule(KotlinModule())
    }


    override fun attemptAuthentication(req: HttpServletRequest,
                                       res: HttpServletResponse): Authentication {
        val creds = mapper.readValue(req.inputStream, User::class.java)
        return manager.authenticate(
                UsernamePasswordAuthenticationToken(
                        creds.name,
                        creds.password,
                        ArrayList<GrantedAuthority>())
        )
    }

    override fun successfulAuthentication(req: HttpServletRequest,
                                          res: HttpServletResponse,
                                          chain: FilterChain,
                                          auth: Authentication) {
        val user = auth.principal as User
        val token = tokenProvider.createToken(user.name, listOf("USER"))

        res.addHeader("Authorization", "Bearer $token")
    }
}