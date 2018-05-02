package betman.security

import betman.db.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class DbUserDetailsService @Autowired constructor(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username != null) {
            val user = userRepository.get(username)
            if (user != null) {
                return org.springframework.security.core.userdetails.User.withUsername(user.name).
                        password(user.password).authorities("USER").build()
            }
        }
        throw UsernameNotFoundException("User not found")
    }
}