import fi.iki.lahtela.betman.Game
import fi.iki.lahtela.betman.GamesController
import org.junit.Assert.assertEquals
import org.junit.Test


class GamesControllerTest {
    @Test
    fun availableGames() {
        val games = GamesController()
        val list: List<Game> = games.all()
        assertEquals(list.size, GamesController.REGULAR_GAMES)
        assertEquals("Russia", list[0].home.name)
    }
}