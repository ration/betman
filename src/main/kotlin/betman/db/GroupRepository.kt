package betman.db

import betman.pojos.Group

interface GroupRepository {
    fun create(group: Group): Group
}
