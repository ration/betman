package betman.db

import betman.pojos.Team

interface TeamRepository {
    fun create(team: Team)
}
