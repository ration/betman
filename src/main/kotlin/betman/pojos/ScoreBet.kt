package betman.pojos

import com.fasterxml.jackson.annotation.JsonProperty

data class ScoreBet(@JsonProperty("id") val matchId: Int,
                    @JsonProperty("home") var home: Int,
                    @JsonProperty("away") var away: Int)