package betman.controller

import betman.db.CacheRepository
import betman.pojos.Group
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class CacheRepositoryTest {
    private var counter = AtomicInteger(0)
    private lateinit var cache: CacheRepository.Cache<String, Group>
    private lateinit var repo: CacheRepository

    @Before
    fun before() {
        repo = CacheRepository()
        cache = repo.getOrCreate("myIdCache") {
            counter.incrementAndGet()
            Single.just(Group(name = it, game = "some"))
        }
    }


    @Test
    fun create() {
        assertEquals("some", cache.get("name").blockingGet().game)
        assertEquals("some", cache.get("name").blockingGet().game)
        assertEquals(1, counter.get())

        assertEquals("some", cache.get("name").blockingGet().game)
        repo.invalidateAll()
        assertEquals("some", cache.get("name").blockingGet().game)
        assertEquals(2, counter.get())

        assertEquals("some", cache.get("name").blockingGet().game)
        repo.invalidateAll("myIdCache")
        assertEquals("some", cache.get("name").blockingGet().game)
        assertEquals(3, counter.get())

        assertEquals("some", cache.get("name").blockingGet().game)
        repo.invalidate("myIdCache", "name")
        assertEquals("some", cache.get("name").blockingGet().game)
        assertEquals(4, counter.get())

    }
}