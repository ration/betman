package betman.db.exposed

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

class TeamDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TeamDao>(Teams)

    var game by GameDao referencedOn Teams.game
    var name by Teams.name
    var iso by Teams.iso
    var externalId by Teams.externalId
}

class MatchDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MatchDao>(Matches)

    var externalId by Matches.externalId
    var game by GameDao referencedOn Matches.game
    var home by TeamDao referencedOn Matches.home
    var away by TeamDao referencedOn Matches.away
    var awayGoals by Matches.awayGoals
    var homeGoals by Matches.homeGoals
    var date by Matches.date
}