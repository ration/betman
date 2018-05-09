package betman.db.exposed

import betman.InvalidKeyException
import betman.InvalidRequestException
import betman.RxUtils.maybeNull
import betman.UnknownGroupException
import betman.UnknownUserException
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
        return Completable.complete()
    }

    override fun get(username: String): Single<List<Group>> {
        return Observable.just(transaction {
            val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
            GroupUserDao.find { (user eq userDao.id) }.map {
                val groupDao = GroupDao.findById(it.group.value)!!
                toGroup(groupDao, GameDao.findById(groupDao.game.value)!!.name, it.name)
            }.toList()
        }).single(listOf())
    }

    override fun get(group: String, username: String): Maybe<Group> {
        return Maybes.maybeNull(transaction {
            val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
            val groupDao = GroupDao.find { Groups.name eq group }.firstOrNull() ?: throw UnknownGroupException()
            GroupUserDao.find { (user eq userDao.id) and (GroupUser.group eq groupDao.id) }.map {
                toGroup(groupDao, GameDao.findById(groupDao.game.value)!!.name, it.name)
            }.singleOrNull()
        })
    }

    override fun updateDisplayName(group: String, username: String, displayName: String) {
        transaction {
            val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
            val groupDao = GroupDao.find { Groups.name eq group }.firstOrNull() ?: throw UnknownGroupException()
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
            toGroup(groupDao, GameDao.findById(groupDao.game.value)!!.name, dao.name)
        }).singleOrError()
    }

    override fun create(newGroup: Group, newKey: String): Single<Group> {
        return Observable.just(transaction {
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
            }
            toGroup(group, gameDao.name)
        }).singleOrError()
    }

    private fun toGroup(group: GroupDao, gameName: String, userDisplayName: String? = null): Group {
        return Group(id = group.id.value,
                name = group.name,
                description = group.description,
                key = group.key,
                game = gameName,
                userDisplayName = userDisplayName,
                winnerPoints = group.winnerPoints,
                goalKingPoints = group.goalKingPoints,
                teamGoalPoints = group.teamGoalPoints,
                exactScorePoints = group.exactScorePoints
        )
    }

}


