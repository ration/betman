package betman.api

interface JsonLoader {
    fun <T> fetch(url: String, type: Class<T>): T
}