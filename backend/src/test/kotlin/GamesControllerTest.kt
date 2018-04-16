import fi.iki.lahtela.betman.Game
import fi.iki.lahtela.betman.GamesController
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test


class GamesControllerTest {
    val games = GamesController()

    @Test
    fun availableGames() {
        val list: List<Game> = games.all()
        assertEquals(list.size, GamesController.REGULAR_GAMES)
        assertEquals("Russia", list[0].home.name)
    }

    @Test
    fun remoteGameData() {
        val json = games.fetchRemote()
        assertNotNull(json)
    }

}