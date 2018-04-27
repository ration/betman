package betman

import betman.db.GroupRepository
import com.nhaarman.mockito_kotlin.capture
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
class GroupControllerTest {

    @InjectMocks
    lateinit var controller: GroupController

    @Mock
    lateinit var groupRepository: GroupRepository

    @Captor
    lateinit var captor: ArgumentCaptor<String>

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun newGroup() {
        val name = "name"
        val description = "description"

        controller.new(name, description)

        verify(groupRepository, times(1)).create(eq(name), eq(description), capture(captor))
        Assert.assertEquals(32, captor.value.length)

    }
}