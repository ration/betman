package betman.pojos

data class Game(val id: Int? = null,
                val name: String,
                val description: String,
                val matches: Map<Int, Match> = mapOf(),
                val other: List<Other> = listOf()) {

    fun withData(matches: Map<Int, Match>, other: List<Other>): Game {
        return Game(id, name, description, matches, other)
    }
}
