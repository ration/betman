package betman

import betman.api.JsonLoader
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class RemoteJsonLoader : JsonLoader {
    private final val logger = KotlinLogging.logger {}

    override fun <T> fetch(remoteUrl: String, type: Class<T>): T? {
        val template = RestTemplate()
        logger.info("Fetching remote data")
        template.messageConverters.filterIsInstance<MappingJackson2HttpMessageConverter>().forEach { it.supportedMediaTypes = listOf(MediaType.TEXT_PLAIN) }
        try {
            return template.getForObject(remoteUrl, type)
        } catch (e: Exception) {
            logger.error("Remote fetching failed", e)
            throw e
        }
    }

}