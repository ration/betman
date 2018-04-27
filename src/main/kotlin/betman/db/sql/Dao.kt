package betman.db.sql

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass


class GroupDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GroupDao>(Groups)

    var description by Groups.description
    var name by Groups.name
    var key by Groups.key
}

class GameDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GameDao>(Games)

    var name by Games.name
    var description by Games.description
}