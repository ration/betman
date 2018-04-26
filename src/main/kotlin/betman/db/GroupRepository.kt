package betman.db

import betman.pojos.Group

interface GroupRepository {
    fun create(name: String, description: String, key: String): Group
}
