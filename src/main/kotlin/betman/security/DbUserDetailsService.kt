package betman.security

import betman.db.UserRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
@Qualifier("dbUser")
class DbUserDetailsService @Autowired constructor(private val userRepository: UserRepository) : UserDetailsService {

    private final val logger = KotlinLogging.logger {}

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username != null) {
            return userRepository.get(username).map {
                org.springframework.security.core.userdetails.User.withUsername(it.name).password(it.password).authorities("USER").build()
            }.doOnComplete {
                logger.info("Failed authentication attempt from user $username - not in database.")
                throw UsernameNotFoundException("User not found")
            }.blockingGet()
        }
        logger.info("Username not set $username?")

        throw UsernameNotFoundException("User not found")
    }
}