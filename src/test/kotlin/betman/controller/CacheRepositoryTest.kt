package betman.controller

import betman.db.CacheRepository
import betman.pojos.Group
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CacheRepositoryTest {
    private var counter = 0
    private lateinit var cache: CacheRepository.Cache<String,Group>

    @Before
    fun before() {
        cache = CacheRepository.getOrCreate("groupCache") {
            ++counter
            Single.just(Group(name=it, game="some"))
        }
    }

    @Test
    fun create() {
        assertEquals("some", cache.get("name").blockingGet().game)
        assertEquals("some", cache.get("name").blockingGet().game)
        assertEquals(1, counter)

        assertEquals("some", cache.get("name").blockingGet().game)
        CacheRepository.invalidateAll()
        assertEquals("some", cache.get("name").blockingGet().game)
        assertEquals(2, counter)

        assertEquals("some", cache.get("name").blockingGet().game)
        CacheRepository.invalidateAll("groupCache")
        assertEquals("some", cache.get("name").blockingGet().game)
        assertEquals(3, counter)

        assertEquals("some", cache.get("name").blockingGet().game)
        CacheRepository.invalidate("groupCache", "name")
        assertEquals("some", cache.get("name").blockingGet().game)
        assertEquals(4, counter)
    }
}