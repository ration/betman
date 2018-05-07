package betman

import betman.api.JsonLoader
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.io.InputStream

/**
 * Loader for JSON data from resources
 */
@Component("FileJsonLoader")
class FileJsonLoader: JsonLoader {

    @Autowired
    lateinit var resourceLoader: ResourceLoader

    override fun <T> fetch(remoteUrl: String, type: Class<T>): T {
        return load(resourceLoader.getResource(remoteUrl).inputStream, type)
    }

    companion object {
        fun <T> load(input: InputStream, type: Class<T>): T {
            val mapper = ObjectMapper()
            return mapper.readValue(input, type)
        }
    }

}