package betman.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties
class Settings {

    @NestedConfigurationProperty
    var db = Db()

    inner class Db(
            var url: String? = null,
            var username: String? = null,
            var password: String? = null
    )
}
