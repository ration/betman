package betman

import betman.lsv.LsvAdapter
import betman.pojos.Game
import org.junit.Assert.assertEquals
import org.junit.Test


class GamesControllerTest {
    val games = GamesController()

    @Test
    fun availableGames() {
        val list: List<Game> = games.all()
        assertEquals(list.size, LsvAdapter.REGULAR_GAMES)
        assertEquals("Russia", list[0].home.name)
    }



}