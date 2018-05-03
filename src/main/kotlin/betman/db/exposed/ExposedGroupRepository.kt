package betman.db.exposed

import betman.InvalidKeyException
import betman.InvalidRequestException
import betman.UnknownUserException
import betman.db.GroupRepository
import betman.db.exposed.Groups.key
import betman.db.exposed.Users.name
import betman.pojos.Group
import io.reactivex.Observable
import io.reactivex.Single
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class ExposedGroupRepository : GroupRepository {

    override fun join(inviteKey: String, displayName: String, username: String): Single<Group> {
        return Observable.just(transaction {
            val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
            val groupDao = GroupDao.find { key eq inviteKey }.firstOrNull() ?: throw InvalidKeyException("Unknown group key")
            val dao = GroupUserDao.new {
                user = userDao.id
                group = groupDao.id
                name = displayName
            }
            commit()
            toGroup(groupDao, dao.name)
        }).singleOrError()
    }

    override fun create(newGroup: Group, newKey: String): Single<Group> {
        return Observable.just(transaction {
            val gameDao: GameDao = GameDao.findById(newGroup.game) ?: throw InvalidRequestException("Unknown game id")
            val group: GroupDao = GroupDao.new {
                name = newGroup.name
                description = newGroup.description
                key = newKey
                game = gameDao.id
            }
            toGroup(group)
        }).singleOrError()
    }

    private fun toGroup(group: GroupDao, userDisplayName: String? = null): Group {
        return Group(id = group.id.value,
                name = group.name,
                description = group.description,
                key = group.key,
                game = group.game.value,
                userDisplayName = userDisplayName)
    }
}


