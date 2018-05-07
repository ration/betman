package betman.controller

import betman.db.UserRepository
import betman.pojos.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus


@Component
@RequestMapping("/api/users")
class UserController @Autowired constructor(private val userRepository: UserRepository
) {
    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.OK)
    fun register(@RequestBody user: User) {
        userRepository.register(user)
    }

}