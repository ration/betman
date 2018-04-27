package betman.pojos

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

data class Match(val id: Int,
                 val home: Team,
                 val away: Team,
                 val description: String,
                 val awayGoals: Int? = null,
                 val homeGoals: Int? = null,
                 @JsonFormat(shape = JsonFormat.Shape.STRING) val date: Date)

