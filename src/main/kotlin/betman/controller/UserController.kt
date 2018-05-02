package betman.controller

import betman.db.UserRepository
import betman.pojos.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping


@Component
@RequestMapping("/api/users")
class UserController @Autowired constructor(private val userRepository: UserRepository,
                                            private val userDetailsService: UserDetailsService) {
    fun register(user: User) {
        userRepository.register(user)
    }

}