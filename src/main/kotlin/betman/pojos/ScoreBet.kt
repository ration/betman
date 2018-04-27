package betman.pojos

import com.fasterxml.jackson.annotation.JsonProperty

data class ScoreBet(@JsonProperty("id") val id: Int,
                    @JsonProperty("home") val home: Int,
                    @JsonProperty("away") val away: Int)