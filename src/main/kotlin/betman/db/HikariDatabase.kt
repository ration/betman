package betman.db

import betman.config.Settings
import betman.db.exposed.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.sql.Connection

@Component
class HikariDatabase @Autowired constructor(private val settings: Settings) : betman.db.Database {

    private final val logger = KotlinLogging.logger {}

    private final val config = HikariConfig()
    final override val datasource: HikariDataSource

    init {
        config.jdbcUrl = this.settings.db.url
        config.username = this.settings.db.username
        config.password = this.settings.db.password
        this.datasource = HikariDataSource(config)
    }

    override fun connect(): Connection {
        return Database.connect(datasource).connector()
    }

    fun close() {
        datasource.close()
    }

    override fun create() {
        logger.info("Loading database")
        transaction {
            SchemaUtils.create(Matches, Odds, Teams, Groups, Users, GroupUser, Games, GroupGame)
        }
    }

}