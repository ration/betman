package betman.security

import betman.db.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
@Qualifier("dbUser")
class DbUserDetailsService @Autowired constructor(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username != null) {
            return userRepository.get(username).map {
                org.springframework.security.core.userdetails.User.withUsername(it.name).password(it.password).authorities("USER").build()
            }.doOnComplete { throw UsernameNotFoundException("User not found") }.blockingGet()
        }
        throw UsernameNotFoundException("User not found")
    }
}