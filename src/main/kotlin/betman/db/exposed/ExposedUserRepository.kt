package betman.db.exposed

import betman.RxUtils.maybeNull
import betman.db.InvalidRequestException
import betman.db.UserAlreadyTakenException
import betman.db.UserRepository
import betman.db.exposed.Users.name
import betman.pojos.User
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Maybes
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class ExposedUserRepository @Autowired constructor(private val passwordEncoder: PasswordEncoder) : UserRepository {
    override fun get(name: String): Maybe<User> {
        return Maybes.maybeNull(transaction {
            UserDao.find { Users.name eq name }.map { toUser(it) }.firstOrNull()
        })
    }

    override fun register(user: User): Single<User> {
        if (user.password == null) {
            throw InvalidRequestException("Password required")
        }
        return Observable.just(transaction {
            if (!UserDao.find { name eq user.name }.empty()) {
                throw UserAlreadyTakenException()
            }
            val dao = UserDao.new {
                name = user.name
                password = encode(user.password)
            }
            toUser(dao)
        }).singleOrError()
    }

    private fun toUser(dao: UserDao) = User(dao.id.value, dao.name)

    private fun encode(password: String): String {
        return passwordEncoder.encode(password)
    }


}