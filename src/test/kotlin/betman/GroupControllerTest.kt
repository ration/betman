package betman

import betman.db.GroupRepository
import betman.pojos.Game
import betman.pojos.Group
import com.nhaarman.mockito_kotlin.*
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
        val description = "description"
        val name = "name"
        controller.new(name, description)
        verify(groupRepository, times(1)).create(any())
    }
}