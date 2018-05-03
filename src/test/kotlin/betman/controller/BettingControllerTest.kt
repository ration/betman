package betman.controller

import betman.db.BettingRepository
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(loader = AnnotationConfigContextLoader::class)
class BettingControllerTest {


    @Mock
    lateinit var bettingRepository: BettingRepository

    @InjectMocks
    lateinit var betting: BettingController


    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun create() {
        assertNotNull(betting)
    }


}