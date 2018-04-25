package betman.pojos

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.Date

data class Game(val id: Int,
                val home: Team,
                val away: Team,
                val description: String,
                @JsonFormat(shape = JsonFormat.Shape.STRING) val date: Date)

