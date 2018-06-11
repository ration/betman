package betman.pojos

data class User(val id: Int? = null, val name: String, val password: String? = null, val groups: List<Group> = listOf(),
                val token: String? = null, val admin: Boolean = false)

