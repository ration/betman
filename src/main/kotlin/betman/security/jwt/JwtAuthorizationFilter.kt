package betman.security.jwt

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthorizationFilter(manager: AuthenticationManager, private val tokenProvider: JwtTokenProvider) : BasicAuthenticationFilter(manager) {
    override fun doFilterInternal(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?) {
        val token = tokenProvider.resolveToken(request as HttpServletRequest)
        if (token != null && tokenProvider.validateToken(token)) {
            val auth = tokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = auth
        }
        chain?.doFilter(request, response)
    }

}
