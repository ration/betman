package betman.pojos

data class Score(val user: String, val points: Int)

data class Standings(val scores: List<Score>)
