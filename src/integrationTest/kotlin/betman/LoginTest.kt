package betman

import betman.pojos.User
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.function.client.WebClient

@RunWith(SpringRunner::class)
@SpringBootTest
class LoginTest {

    //  WebTestClient not available, see comment in that file

    val client: WebClient = WebClient.create("h")

    @Test
    fun register() {
        val user = User(name = "name", password = "password")
        client.post().uri("/api/users/register").syncBody(user).exchange().block()

    }
}
