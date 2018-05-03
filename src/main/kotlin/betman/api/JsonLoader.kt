package betman.api

interface JsonLoader {
    fun <T> fetch(remoteUrl: String, type: Class<T>): T
}