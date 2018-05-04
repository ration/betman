package betman.security.jwt

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthorizationFilter(manager: AuthenticationManager, private val provider: JwtTokenProvider) : BasicAuthenticationFilter(manager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest,
                                  res: HttpServletResponse,
                                  chain: FilterChain) {
        val header = req.getHeader("Authorization")

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res)
            return
        }

        val authentication = getAuthentication(req)

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, res)
    }

    private fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = provider.resolveToken(request)
        return if (token != null && provider.validateToken(token)) {
            return provider.getAuthentication(token)
        } else {
            null
        }
    }


}
