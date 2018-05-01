package betman.db.exposed

import betman.db.UserRepository
import betman.pojos.User

class ExposedUserRepository : UserRepository {
    override fun register(user: User): User {
        UserDao
        return user
    }
}