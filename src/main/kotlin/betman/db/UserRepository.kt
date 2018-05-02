package betman.db

import betman.pojos.User

interface UserRepository {
    fun register(user: User): User
    fun get(name: String): User?
}