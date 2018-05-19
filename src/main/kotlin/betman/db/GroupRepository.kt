package betman.db

import betman.pojos.Group
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface GroupRepository {
    fun create(newGroup: Group, newKey: String, username: String): Single<Group>

    fun update(group: Group, username: String): Completable

    /** Join given group. Can also be used to update the name */
    fun join(inviteKey: String, username: String, displayName: String): Single<Group>

    /**
     * Update display name of user
     */
    fun updateDisplayName(group: String, username: String, displayName: String)

    fun get(groupKey: String, username: String?): Maybe<Group>

    fun get(username: String): Single<List<Group>>

}
