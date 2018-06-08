package betman.db.exposed

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass


class GroupDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GroupDao>(Groups)

    var description by Groups.description
    var name by Groups.name
    var key by Groups.key
    var game by Groups.game
    var winnerPoints by Groups.winnerPoints
    var goalKingPoints by Groups.goalKingPoints
    var teamGoalPoints by Groups.teamGoalPoints
    var exactScorePoints by Groups.exactScorePoints
    var correctWinnerPoints by Groups.correctWinnerPoints
    var owner by Groups.owner
}

class GameDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GameDao>(Games)

    var name by Games.name
    var description by Games.description
    val goalKing by Games.goalKing
    val winner by Games.winner
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
    var home by TeamDao optionalReferencedOn Matches.home
    var away by TeamDao optionalReferencedOn Matches.away
    var awayGoals by Matches.awayGoals
    var homeGoals by Matches.homeGoals
    var date by Matches.date
    var description by Matches.description
    var homeOdds by Matches.homeOdds
    var awayOdds by Matches.awayOdds
}

class UserDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDao>(Users)

    var name by Users.name
    var password by Users.password
}


class GroupUserDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GroupUserDao>(GroupUser)

    var group by GroupUser.group
    var user by GroupUser.user
    var name by GroupUser.name
}

class BetDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BetDao>(Bets)

    var match by Bets.match
    var user by Bets.user
    var home by Bets.home
    var away by Bets.away
    var group by Bets.group
    var winner by Bets.winner
    var goalKing by Bets.goalking
}

