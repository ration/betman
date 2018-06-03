package betman

import betman.pojos.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.FluxExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient


/** Sadly these tests are a bit coupled - you need to run them all in order. Should rather just call chain the prereqs in each test */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ApiTest : IntegrationTest() {

    private val user = User(name = "name", password = "password")
    private val authHeader = "Authorization"


    companion object {
        var session: String? = null
        var group: Group? = null
        var bet: Bet? = null
        var matches: List<Match> = listOf()
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
        val newGroup = Group(game = "Fifa2018", name = "My Group", description = "My Description", userDisplayName = "myName", admin = user.name)
        val groupFlex: FluxExchangeResult<Group> = client.post().uri("/api/groups/new").header(authHeader, session)
            .syncBody(newGroup).exchange()
            .expectStatus().isOk.returnResult(Group::class.java)
        group = groupFlex.responseBody.blockFirst()
        assertNotNull(group?.key)
    }

    @Test
    fun test04Groups() {
        val result: Groups = get("/api/groups/")
        assertEquals("Fifa2018", result.groups[0].game)
    }

    @Test
    fun test05UpdateGroup() {
        group?.name = "newName"
        post("/api/groups/update", group).expectStatus().isOk
    }

    @Test
    fun test06Bets() {
        bet = get("/api/bets/${group?.key}")
        assertNotNull(bet)
    }

    @Test
    fun test08Games() {
        val result: Game = get("/api/games/Fifa2018")
        assertNotNull(result)
        matches = result.matches
    }

    @Test
    fun test09UpdateBets() {
        bet?.scores = listOf(ScoreBet(matchId = matches[0].id, home = 10, away = 11))
        post("/api/bets/update?group=${group?.key}", bet)
    }

    @Test
    fun test10Status() {
        val res: User = get("/api/users/status")
        assertNotNull(res)

    }

    private fun <T> post(uri: String, body: T): WebTestClient.ResponseSpec {
        return client.post().uri(uri).header(authHeader, session).syncBody(body).exchange().expectStatus().isOk
    }

    /**
     * Just the body of any get request
     */
    private inline fun <reified T> get(uri: String): T {
        return client.get().uri(uri).header(authHeader, session).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk.returnResult(T::class.java).responseBody.blockFirst()
                ?: throw InvalidRequestException("get failed")
    }

}
