package betman.db

import betman.pojos.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface UserRepository {
    fun register(user: User): Single<User>
    fun get(name: String): Maybe<User>
    fun update(user: User): Completable
}