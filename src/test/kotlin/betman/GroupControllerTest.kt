package betman

import org.junit.Assert
import org.junit.Test


class GroupControllerTest {
    @Test
    fun newInviteKey() {
        var controller = GroupController()
        val invite: String = controller.newInvite().key
        Assert.assertEquals(32, invite.length)
    }
}