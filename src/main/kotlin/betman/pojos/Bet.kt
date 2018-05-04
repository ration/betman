package betman.pojos

data class Bet(val gameId: String,
               val scores: List<ScoreBet> = listOf(), val places: List<PlaceBet> = listOf(), val others: List<OtherBet> = listOf())