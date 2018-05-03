package betman

import io.reactivex.Maybe
import io.reactivex.rxkotlin.Maybes

object RxUtils {
    fun <T> Maybes.maybeNull(value: T?): Maybe<T> {
        return value?.let { Maybe.just(value) } ?: Maybe.empty<T>()

    }
}