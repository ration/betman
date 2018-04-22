package betman.pojos

import com.fasterxml.jackson.annotation.JsonProperty

data class Bet(@JsonProperty("id") val gameId: Int,
               @JsonProperty("home") val home: Int,
               @JsonProperty("away") val away: Int)