package betman.controller

import betman.db.UserRepository
import betman.pojos.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.security.Principal


@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(private val userRepository: UserRepository) {
    private val logger = KotlinLogging.logger {}

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.OK)
    fun register(@RequestBody user: User): Single<User> {
        logger.info("Registering user ${user.name}")
        return userRepository.register(user)
    }


    @GetMapping("/status", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun status(user: Principal): Maybe<User> {
        return userRepository.get(user.name)
    }

    @PostMapping("/update")
    fun update(@RequestBody user: User, principal: Principal): Completable {
        return userRepository.get(principal.name).map{
            if (it.admin) {
                userRepository.update(user)
            } else {
                val update = User(name=principal.name, password = user.password)
                userRepository.update(update)
            }
        }.flatMapCompletable { userRepository.update(user) }
    }


}