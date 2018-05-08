package betman

import betman.pojos.Group
import betman.pojos.Groups
import betman.pojos.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.test.web.reactive.server.FluxExchangeResult


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LoginTest : IntegrationTest() {

    private val user = User(name = "name", password = "password")
    private val authHeader = "Authorization"

    companion object {
        var session: String? = null
    }


    @Test
    fun test01Register() {
        client.post().uri("/api/users/register").syncBody(user).exchange().expectStatus().isOk
    }

    @Test
    fun test02Login() {
        val result: FluxExchangeResult<Unit> = client.post().uri("/api/users/login").syncBody(user).exchange()
            .expectStatus().isOk.expectHeader()
            .exists(authHeader).returnResult(Unit::class.java)
        session = result.responseHeaders[authHeader]?.get(0)
        assertNotNull(session)
    }

    @Test
    fun test03newGroup() {
        val group = Group(game = "Fifa2018", name = "My Group", description = "My Description", userDisplayName = "myName")
        val groupFlex: FluxExchangeResult<Group> = client.post().uri("/api/groups/new").header(authHeader, session)
            .syncBody(group).exchange()
            .expectStatus().isOk.returnResult(Group::class.java)
        assertNotNull(groupFlex.responseBody.blockFirst()?.key)
    }

    @Test
    fun test04Groups() {
        val result: FluxExchangeResult<Groups> = client.get().uri("/api/groups/").header(authHeader, session)
            .exchange()
            .expectStatus().isOk.returnResult(Groups::class.java)
        assertEquals("Fifa2018", result.responseBody.blockFirst()?.groups?.get(0)?.game)
    }


}
