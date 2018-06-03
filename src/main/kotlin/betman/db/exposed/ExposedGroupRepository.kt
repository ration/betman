package betman.db.exposed

import betman.*
import betman.RxUtils.maybeNull
import betman.db.GroupRepository
import betman.db.exposed.GroupUser.user
import betman.db.exposed.Groups.key
import betman.db.exposed.Users.name
import betman.pojos.Group
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Maybes
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class ExposedGroupRepository : GroupRepository {


    override fun update(group: Group, username: String): Completable {
        if (group.key == null) throw InvalidRequestException("Group key required")

        try {
            transaction {
                val groupDao = GroupDao.find { Groups.key eq group.key!! }.first()
                val owner = UserDao.findById(groupDao.owner)!!
                if (owner.name != username) {
                    throw InvalidUserException("$username not group owner")
                }
                groupDao.name = group.name
                groupDao.description = group.description
                groupDao.winnerPoints = group.winnerPoints
                groupDao.goalKingPoints = group.goalKingPoints
                groupDao.teamGoalPoints = group.teamGoalPoints
                groupDao.exactScorePoints = group.exactScorePoints
                commit()
            }
        } catch (e: Exception) {
            return Completable.error(e)
        }
        return Completable.complete()
    }

    override fun get(username: String): Single<List<Group>> {
        return Observable.just(transaction {
            val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
            GroupUserDao.find { (user eq userDao.id) }.map {
                val groupDao = GroupDao.findById(it.group.value)!!
                Converters.toGroup(groupDao, GameDao.findById(groupDao.game.value)!!.name, it.name)
            }.toList()
        }).single(listOf())
    }

    override fun get(groupKey: String, username: String?): Maybe<Group> {
        return Maybes.maybeNull(transaction {
            val groupDao = GroupDao.find { Groups.key eq groupKey }.firstOrNull() ?: throw UnknownGroupException()
            var displayName: String? = null
            if (username != null) {
                val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
                displayName = GroupUserDao.find { (user eq userDao.id) and (GroupUser.group eq groupDao.id) }
                    .map { it.name }.firstOrNull()
            }
            Converters.toGroup(groupDao, GameDao.findById(groupDao.game.value)!!.name, displayName)

        })
    }

    override fun updateDisplayName(group: String, username: String, displayName: String) {
        transaction {
            val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
            val groupDao = GroupDao.find { Groups.key eq group }.firstOrNull() ?: throw UnknownGroupException()
            val dao = GroupUserDao.find { (user eq userDao.id) and (GroupUser.group eq groupDao.id) }.single()
            dao.name = displayName
            commit()
        }
    }

    override fun join(inviteKey: String, username: String, displayName: String): Single<Group> {
        return Observable.just(transaction {
            val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
            val groupDao = GroupDao.find { key eq inviteKey }.firstOrNull()
                    ?: throw InvalidKeyException("Unknown group key")
            val dao = GroupUserDao.new {
                user = userDao.id
                group = groupDao.id
                name = displayName
            }
            commit()
            Converters.toGroup(groupDao, GameDao.findById(groupDao.game.value)!!.name, dao.name)
        }).singleOrError()
    }

    override fun create(newGroup: Group, newKey: String, username: String): Single<Group> {
        return Observable.just(transaction {
            val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
            val gameDao: GameDao = GameDao.find { Games.name eq newGroup.game }.singleOrNull()
                    ?: throw InvalidRequestException("Unknown game id")
            val group: GroupDao = GroupDao.new {
                name = newGroup.name
                description = newGroup.description
                key = newKey
                game = gameDao.id
                winnerPoints = newGroup.winnerPoints
                goalKingPoints = newGroup.goalKingPoints
                teamGoalPoints = newGroup.teamGoalPoints
                exactScorePoints = newGroup.exactScorePoints
                owner = userDao.id
            }
            Converters.toGroup(group, gameDao.name, username)
        }).singleOrError()
    }

}


