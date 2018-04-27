package betman.pojos

data class Game(val id: Int? = null,
                val name: String,
                val description: String,
                val matches: List<Match> = listOf(), val other: List<Other> = listOf())