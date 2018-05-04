package betman.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


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

    @NestedConfigurationProperty
    var security = Security()

    inner class Security(
            var token: Token = Token()
    )

    inner class Token(
            var secretKey: String = "secret-key",
            var expirationTime: Int = 864_000_000
    )

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}
