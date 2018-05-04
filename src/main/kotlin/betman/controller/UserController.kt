package betman.controller

import betman.db.UserRepository
import betman.pojos.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping


@Component
@RequestMapping("/api/users")
class UserController @Autowired constructor(private val userRepository: UserRepository,
                                            @Qualifier("dbUser") private val userDetailsService: UserDetailsService) {
    @PostMapping("/register")
    fun register(@RequestBody user: User) {
        userRepository.register(user)
    }
}