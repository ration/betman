package betman.db.exposed

import betman.config.Settings
import betman.db.HikariDatabase
import betman.db.CacheRepository
import betman.pojos.Group
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import java.sql.Connection
import java.time.Instant


open class DbTest {
    private lateinit var connection: Connection
    private lateinit var factory: HikariDatabase

    @Before
    fun loadDb() {
        val jdbcUrl = "jdbc:h2:mem:creation_test"
        val settings = Settings()
        settings.db.url = jdbcUrl
        factory = HikariDatabase(settings)
        connection = factory.connect()
        factory.create()
    }


    @After
    fun unload() {
        connection.close()
        factory.close()
        CacheRepository.instance.invalidateAll()
    }

    protected fun createGame(gameName: String = "game"): GameDao = transaction {
        GameDao.new {
            name = gameName
            description = "description"
        }
    }

    protected fun createUser(userName: String) = transaction {
        UserDao.new {
            name = userName
            password = "password"
            admin = false
        }
    }

    protected fun createGroup(groupName: String, groupKey: String, gameDao: GameDao, user: String) = transaction {
        ExposedGroupRepository().create(Group(name = groupName, key = groupKey, game = gameDao.name, admin = "user"), groupKey, user)
    }

    protected fun createMatch(gameDao: GameDao, homeDao: TeamDao, awayDao: TeamDao,
                              ext: Int, time: Long = Instant.now().plusSeconds(1000).toEpochMilli()): MatchDao = transaction {
        CacheRepository.instance.invalidateAll()

        MatchDao.new {
            game = gameDao
            externalId = ext
            home = homeDao
            away = awayDao
            date = time
            description = "some"
            homeGoals = 2
            awayGoals = 1
        }
    }

    protected fun createTeam(gameDao: GameDao, team: String, ext: Int): TeamDao {
        return transaction {
            TeamDao.new {
                game = gameDao
                name = team
                iso = "xx"
                externalId = ext
            }
        }
    }

}