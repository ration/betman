package betman.db.exposed

import betman.*
import betman.RxUtils.maybeNull
import betman.db.CacheRepository
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

    private val cache = CacheRepository.instance.getOrCreate<String, Group>("groupCache") {
        getFromDb(it, null).toSingle()
    }
    private val displayNameCache = CacheRepository.instance.getOrCreate<Pair<String,String>, String?>("displayNameCache") {
        Single.just(usernameFromDb(it.first, it.second))
    }
    private val groupsCache = CacheRepository.instance.getOrCreate<String,List<Group>>("groupsCache") {
        getGroupsFromDb(it)
    }


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
                groupDao.correctWinnerPoints = group.correctWinnerPoints
                commit()
                CacheRepository.instance.invalidateAll()
            }
        } catch (e: Exception) {
            return Completable.error(e)
        }
        return Completable.complete()
    }

    override fun get(username: String): Single<List<Group>> {
        return groupsCache.get(username).toSingle()
    }

    private fun getGroupsFromDb(username: String): Single<List<Group>> {
        return Observable.just(transaction {
            val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
            GroupUserDao.find { (user eq userDao.id) }.map {
                val groupDao = GroupDao.findById(it.group.value)!!

                Converters.toGroup(groupDao, GameDao.findById(groupDao.game.value)!!.name, it.name)
            }.toList()
        }).single(listOf())
    }

    override fun get(groupKey: String, username: String?): Maybe<Group> {
        return cache.get(groupKey).map {
            it.userDisplayName = getUserName(username, it.key)
            it
        }
    }

    fun getUserName(username: String?, key: String?): String? {
        return if (username != null && key != null) {
            displayNameCache.get(Pair(username, key)).blockingGet()
        } else {
            null
        }
    }

    private fun usernameFromDb(username: String?, key: String?): String? {
        return transaction {
            if (username != null && key != null) {
                val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
                val groupDao = GroupDao.find { Groups.key eq key }.firstOrNull() ?: throw UnknownGroupException()
                GroupUserDao.find { (user eq userDao.id) and (GroupUser.group eq groupDao.id) }
                        .map { it.name }.firstOrNull()
            } else {
                null
            }
        }
    }

    fun getFromDb(groupKey: String, username: String?): Maybe<Group> {
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
            verifyExisting(displayName, userDao)
            val dao = GroupUserDao.find { (user eq userDao.id) and (GroupUser.group eq groupDao.id) }.single()

            dao.name = displayName
            commit()
            cache.invalidateAll()
            groupsCache.invalidateAll()
            displayNameCache.invalidate(Pair(username, group))
        }
    }

    private fun verifyExisting(displayName: String, userDao: UserDao) {
        val existing = GroupUserDao.find { (GroupUser.name eq displayName) }.singleOrNull()
        if (existing != null && existing.user != userDao.id) {
            throw UserAlreadyTakenException("Group already has name $displayName")
        }
    }

    override fun join(inviteKey: String, username: String, displayName: String): Single<Group> {
        return Observable.just(transaction {
            val userDao = UserDao.find { name eq username }.firstOrNull() ?: throw UnknownUserException()
            val groupDao = GroupDao.find { key eq inviteKey }.firstOrNull()
                    ?: throw InvalidKeyException("Unknown group key")
            verifyExisting(displayName, userDao)
            val dao = GroupUserDao.new {
                user = userDao.id
                group = groupDao.id
                name = displayName
            }
            CacheRepository.instance.invalidateAll()
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
                correctWinnerPoints = newGroup.correctWinnerPoints
                owner = userDao.id
            }
            cache.invalidateAll()

            Converters.toGroup(group, gameDao.name, username)
        }).singleOrError()
    }

    override fun chart(groupKey: String): Single<Map<String, Map<Int, Int>>> {
        return get(groupKey, null).map {
            Converters.chart(it)
        }.toSingle()
    }
}

