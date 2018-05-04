package betman.security.jwt

import betman.InvalidTokenException
import betman.config.Settings
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider @Autowired constructor(private val settings: Settings,
                                              @Qualifier("dbUser") private val detailsService: UserDetailsService) {
    fun createToken(username: String, roles: List<String>): String {

        val claims = Jwts.claims().setSubject(username)
        claims.put("auth", roles.stream().map { s -> SimpleGrantedAuthority(s) }.filter { Objects.nonNull(it) })

        val now = Date()
        val validity = Date(now.time + settings.security.token.expirationTime)

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, settings.security.token.secretKey)//
                .compact()
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7, bearerToken.length)
        } else null
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = detailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUsername(token: String): String {
        return Jwts.parser().setSigningKey(settings.security.token.secretKey).parseClaimsJws(token).getBody().getSubject()
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(settings.security.token.secretKey).parseClaimsJws(token)
            return true
        } catch (e: JwtException) {
            throw InvalidTokenException("Expired or invalid JWT token")
        } catch (e: IllegalArgumentException) {
            throw InvalidTokenException("Expired or invalid JWT token")
        }

    }

}