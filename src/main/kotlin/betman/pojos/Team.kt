package betman.pojos

data class Team(val name: String, val iso: String) {
    companion object {
        val UNKNOWN_TEAM = Team("Unknown", "de")
    }
}


