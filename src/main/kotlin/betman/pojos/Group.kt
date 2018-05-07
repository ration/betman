package betman.pojos

data class Group(var id: Int? = null,
                 var name: String,
                 var description: String? = null,
                 var key: String? = null,
                 var userDisplayName: String? = null,
                 val game: String)