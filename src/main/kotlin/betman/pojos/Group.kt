package betman.pojos

data class Group(var id: Int? = null,
                 var name: String,
                 var description: String? = null,
                 var key: String? = null,
                 var userDisplayName: String? = null,
                 val game: String,
                 var standings: List<Score> = listOf(),
                 val admin: String,
                 val winnerPoints: Int = 5,
                 val goalKingPoints: Int = 5,
                 val teamGoalPoints: Int = 1,
                 val exactScorePoints: Int = 5)
