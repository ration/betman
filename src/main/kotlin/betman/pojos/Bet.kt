package betman.pojos

data class Bet(val groupKey: String,
               val scores: Map<Int, ScoreBet> = mapOf(),
               val places: List<PlaceBet> = listOf(),
               val goalKing: String? = null,
               val winner: Int? = null)