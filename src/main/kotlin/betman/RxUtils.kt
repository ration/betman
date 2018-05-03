package betman

import io.reactivex.Maybe

class RxUtils {
    companion object {
        /**
         * Allows nullable in maybeNull creation
         */
        fun <T> maybeNull(value: T?): Maybe<T> {
            return value?.let { Maybe.just(value) } ?: Maybe.empty<T>()
        }
    }
}