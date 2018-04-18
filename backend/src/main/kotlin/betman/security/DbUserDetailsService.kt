package betman.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class DbUserDetailsService : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("not implemented")
    }
}