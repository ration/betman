package betman.controller

import betman.db.GroupRepository
import betman.pojos.Group
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.security.Principal

class GroupControllerTest {

    @Mock
    private lateinit var groupRepository: GroupRepository

    @Mock
    private lateinit var principal: Principal


    @Mock
    private lateinit var auth: Authentication

    @InjectMocks
    private lateinit var controller: GroupController

    private val group = Group(name = "test", description = "desc", game = "game", admin = "user")
    private val username = "myusername"


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        whenever(principal.name).thenReturn(username)
    }

    @Test
    fun new() {
        val newGroup = Group(name = group.name, description = group.name, key = "value", game = "game", admin = "user")
        argumentCaptor<String>().apply {
            whenever(groupRepository.create(any(), any(), any())).thenReturn(Observable.just(newGroup).singleOrError())
            whenever(groupRepository.join(any(), any(), any())).thenReturn(Observable.just(newGroup).singleOrError())

            val ans = controller.new(newGroup, principal).blockingGet()!!
            verify(groupRepository, times(1)).create(eq(newGroup), capture(), any())
            assertNotNull(ans.key)
            assertNotNull(firstValue)
            verify(groupRepository, times(1)).join(eq(ans.key!!), eq(username), any())
        }
    }

    @Test
    fun getGroups() {
        whenever(principal.name).thenReturn(username)
        whenever(groupRepository.get(any())).thenReturn(Observable.just(listOf(group)).singleOrError())
        controller.get(principal)
        verify(groupRepository, times(1)).get(eq(username))
    }

    @Test
    fun join() {
        val key = "mykey"
        val displayName = "jack"
        whenever(groupRepository.join(eq(key), eq(username), eq(displayName))).thenReturn(Observable.just(group).singleOrError())
        val ans = controller.join(key, displayName, principal).blockingGet()
        assertEquals(group, ans)
    }

    @Test
    fun update() {
        val displayName = "newDisplayName"
        controller.updateDisplayName(group.name, displayName, principal)
        verify(groupRepository, times(1)).updateDisplayName(eq(group.name), eq(username), eq(displayName))
    }

    @Test
    fun updateGroup() {
        group.name = "new name"
        whenever(groupRepository.update(eq(group), any())).thenReturn(Completable.complete())
        controller.update(group, principal).blockingGet()
        verify(groupRepository, times(1)).update(any(), any())
    }

    @Test
    fun getSingleAnonymous() {
        val key = "key"
        SecurityContextHolder.getContext().authentication = auth;
        whenever(auth.principal).thenReturn("anonymousUser")

        whenever(groupRepository.get(eq(key), eq(null))).thenReturn(Maybe.just(group))
        controller.get(key).blockingGet()
        verify(groupRepository, times(1)).get(any(), eq(null))
    }

}