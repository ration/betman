package betman.pojos

data class Bet(val groupKey: String,
               val scores: List<ScoreBet> = listOf(),
               val places: List<PlaceBet> = listOf(),
               val goalKing: String? = null,
               val winner: Int? = null)