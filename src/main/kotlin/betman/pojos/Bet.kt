package betman.pojos

data class Bet(var groupKey: String,
               var scores: List<ScoreBet> = listOf(),
               var places: List<PlaceBet> = listOf(),
               var goalKing: String? = null,
               var winner: Int? = null,
               var userDisplayName: String? = null)