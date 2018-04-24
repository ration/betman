package betman.db.sql

import betman.config.Settings
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DbFactory @Autowired constructor(private val settings: Settings) {

    private val config = HikariConfig()
    val datasource: HikariDataSource

    init {
        config.jdbcUrl = this.settings.db.url
        config.username = "betman"
        config.password = "manbet"
        this.datasource = HikariDataSource(config)
    }


}