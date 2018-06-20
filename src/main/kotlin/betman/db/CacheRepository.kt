package betman.db

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import io.reactivex.Maybe
import io.reactivex.Single
import net.javacrumbs.futureconverter.java8rx2.FutureConverter
import java.util.concurrent.TimeUnit


/** Async cache for pojo types */
object CacheRepository {
    private val caches: MutableMap<String, Cache<*, *>> = mutableMapOf()
    var prefix: String = ""


    fun invalidate(type: String, key: String) {
        caches[type]?.invalidate(key)
    }

    fun invalidateAll() {
        caches.values.forEach { it.invalidateAll() }
    }

    fun invalidateAll(type: String) {
        caches[type]?.invalidateAll()
    }


    fun <K, V> getOrCreate(type: String, mapper: (mapperKey: K) -> Single<V>): Cache<K, V> {
        @Suppress("UNCHECKED_CAST")
        return caches.computeIfAbsent(prefix+type) { Cache(mapper) } as Cache<K, V>
    }


    class Cache<K, V> constructor(mapper: (mapperKey: K) -> Single<V>) {
        private val cache: AsyncLoadingCache<K, V> = Caffeine.newBuilder().maximumSize(10_000).expireAfterWrite(1, TimeUnit.DAYS).buildAsync<K, V> { key, _ -> FutureConverter.toCompletableFuture(mapper(key)) }

        fun get(key: K): Maybe<V> {
            return FutureConverter.toSingle(cache.get(key)).toMaybe()
        }

        fun invalidateAll() {
            cache.synchronous().invalidateAll()
        }

        fun invalidate(key: Any) {
            cache.synchronous().invalidate(key)
        }

    }

}

