package betman.security.jwt

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthenticationFilter(manager: AuthenticationManager) : BasicAuthenticationFilter(manager) {
    override fun doFilterInternal(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?) {
        super.doFilterInternal(request!!, response!!, chain!!)
    }
}