package betman.controller

import betman.db.GroupRepository
import betman.pojos.Group
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.security.Principal

class GroupControllerTest {

    @Mock
    private lateinit var groupRepository: GroupRepository

    @Mock
    private lateinit var principal: Principal

    @InjectMocks
    private lateinit var controller: GroupController

    private val group = Group(name = "test", description = "desc", game = 1)


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun new() {
        argumentCaptor<String>().apply {
            whenever(groupRepository.create(any(), any())).thenReturn(Group(name = group.name, description = group.name, key = "value", game = 1))
            val ans = controller.new(group)
            verify(groupRepository, times(1)).create(eq(group), capture())
            assertNotNull(ans.key)
            assertNotNull(firstValue)
        }
    }

    @Test
    fun join() {
        val key = "mykey"
        val userName = "myusername"
        val displayName = "jack"
        whenever(principal.name).thenReturn(userName)
        whenever(groupRepository.join(eq(key), eq(displayName), eq(userName))).thenReturn(group)
        val ans = controller.join(key, displayName, principal)
        assertEquals(group, ans)
    }
}