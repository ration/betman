package betman.db

import betman.pojos.Group
import io.reactivex.Single

interface GroupRepository {
    fun create(newGroup: Group, newKey: String): Single<Group>

    /** Join given group. Can also be used to update the name */
    fun join(inviteKey: String, displayName: String, username: String): Single<Group>
}
