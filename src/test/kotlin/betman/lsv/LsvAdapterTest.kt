package betman.lsv

import org.junit.Assert
import org.junit.Test

class LsvAdapterTest {
    private val lsvData = LsvAdapter()

    @Test
    fun remoteGameData() {
        val json = lsvData.fetchRemote()
        Assert.assertNotNull(json)
    }

    @Test
    fun allGamesAsPojo() {
        val games = lsvData.regularGames()
        Assert.assertEquals(LsvAdapter.REGULAR_GAMES + LsvAdapter.PLAYOFF_GAMES, games.size)
        Assert.assertEquals("Russia", games[0].home.name)
    }
}