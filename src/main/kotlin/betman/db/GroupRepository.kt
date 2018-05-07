package betman.db

import betman.pojos.Group
import io.reactivex.Maybe
import io.reactivex.Single

interface GroupRepository {
    fun create(newGroup: Group, newKey: String): Single<Group>

    /** Join given group. Can also be used to update the name */
    fun join(inviteKey: String, username: String, displayName: String): Single<Group>

    /**
     * Update display name of user
     */
    fun updateDisplayName(group: String, username: String, displayName: String)

    /**
     * Get user related data
     */
    fun get(group: String, username: String): Maybe<Group>

    fun get(username: String): Single<List<Group>>
}
