package betman.db.sql

import betman.db.GroupRepository
import betman.pojos.Group
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator
import org.springframework.stereotype.Component

@Component
class SqlGroupRepository: GroupRepository {
    override fun create(name: String, description: String): Group {
        TODO("not ipmplemented")
    }


    private fun generateKey(): String {
        val generator = RandomValueStringGenerator()
        generator.setLength(32)
        return generator.generate()
    }

}