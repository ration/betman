package betman.db.exposed

import betman.db.GroupRepository
import betman.pojos.Group
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class ExposedGroupRepository : GroupRepository {
    override fun create(group: Group): Group {
        return transaction {
            val group: GroupDao = GroupDao.new {
                name = group.name
                description = group.description
                key = group.key
            }
            commit()
            Group(group.id.value, group.name, group.description, group.key)
        }
    }
}


