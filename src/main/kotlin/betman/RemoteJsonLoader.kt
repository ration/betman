package betman

import betman.api.JsonLoader
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class RemoteJsonLoader : JsonLoader {
    override fun <T> fetch(remoteUrl: String, type: Class<T>): T {
        val template = RestTemplate()
        template.messageConverters.filterIsInstance<MappingJackson2HttpMessageConverter>().forEach { it.supportedMediaTypes = listOf(MediaType.TEXT_PLAIN) }
        return template.getForObject(remoteUrl, type)
    }

}