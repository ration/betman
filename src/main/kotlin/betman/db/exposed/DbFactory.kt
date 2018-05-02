package betman.db.exposed

import betman.config.Settings
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.sql.Connection
import javax.sql.DataSource

@Component
class DbFactory @Autowired constructor(private val settings: Settings) {

    private final val logger = KotlinLogging.logger {}

    private final val config = HikariConfig()
    private val datasource: HikariDataSource

    init {
        config.jdbcUrl = this.settings.db.url
        config.username = this.settings.db.username
        config.password = this.settings.db.password
        this.datasource = HikariDataSource(config)
    }

    fun datasource(): DataSource {
        return this.datasource
    }

    fun connect(): Connection {
        return Database.connect(datasource).connector()
    }

    fun close() {
        datasource.close()
    }

    fun createDb(dataSource: DataSource) {
        logger.info("Loading database")
        transaction {
            SchemaUtils.create(Matches, Teams, Groups, Users, GroupUser, Games)
        }
    }

}