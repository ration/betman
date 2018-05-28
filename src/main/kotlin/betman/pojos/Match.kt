package betman.pojos

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

data class Match(val id: Int,
                 val home: Int,
                 val away: Int,
                 val description: String,
                 var awayGoals: Int? = null,
                 var homeGoals: Int? = null,
                 @JsonFormat(shape = JsonFormat.Shape.STRING) val date: Date)




