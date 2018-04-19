package betman.pojos

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

data class Game(val id: Int,
                val home: Team,
                val away: Team,
                @JsonFormat(shape = JsonFormat.Shape.STRING) val date: Date)

