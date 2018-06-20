package betman.db.exposed

import betman.InvalidRequestException
import betman.InvalidUserException
import betman.RxUtils.maybeNull
import betman.UserAlreadyTakenException
import betman.db.CacheRepository
import betman.db.UserRepository
import betman.db.exposed.Users.name
import betman.pojos.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Maybes
import mu.KotlinLogging
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class ExposedUserRepository @Autowired constructor(private val passwordEncoder: PasswordEncoder) : UserRepository {

    val userCache = CacheRepository.getOrCreate<String, User>("userCache") {
        getFromDb(it).toSingle()
    }

    private val eLogger = KotlinLogging.logger {} // Collides with exposed logger
    override fun get(name: String): Maybe<User> {
        return userCache.get(name)
    }

    fun getFromDb(name: String): Maybe<User> {
        try {

            return Maybes.maybeNull(transaction {
                val user = UserDao.find { Users.name eq name }.map { toUser(it) }.firstOrNull()
                if (user == null) {
                    eLogger.info("Did not find user with $name")
                } else {
                    eLogger.info("Found user")
                }
                commit()
                user
            })
        } catch (e: Exception) {
            eLogger.error("Error retrieving user: ${e.message}", e)
            throw e
        }
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
                admin = false
            }
            commit()
            toUser(dao)

        }).singleOrError()
    }

    override fun update(user: User): Completable {
        return Completable.fromAction {
            transaction {
                val userDao = UserDao.find { Users.name eq user.name }.firstOrNull() ?: throw InvalidUserException("${user.name} not found")
                userDao.password = passwordEncoder.encode(user.password)
                userCache.invalidate(user.name)
            }
        }
    }

    private fun toUser(dao: UserDao) = User(id = dao.id.value, name = dao.name, password = dao.password, admin = dao.admin)

    private fun encode(password: String): String {
        return passwordEncoder.encode(password)
    }


}