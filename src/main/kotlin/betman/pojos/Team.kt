package betman.pojos

data class Team(val id: Int, val name: String, val iso: String) {
    companion object {
        val UNKNOWN_TEAM = Team(-1, "Unknown", "xx")
    }
}


