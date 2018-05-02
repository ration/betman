package betman.db

import betman.pojos.Group

interface GroupRepository {
    fun create(newGroup: Group, newKey: String): Group

    /** Join given group. Can also be used to update the name */
    fun join(inviteKey: String, displayName: String, username: String): Group
}
