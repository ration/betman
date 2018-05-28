package betman.pojos

data class Game(val id: Int? = null,
                val name: String,
                val description: String,
                val matches: List<Match> = listOf(),
                val other: List<Other> = listOf(),
                val teams: List<Team> = listOf()) {

    fun withData(matches: List<Match>, other: List<Other>): Game {
        return Game(id, name, description, matches, other)
    }
}
