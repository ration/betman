package betman.db.sql

import betman.db.GroupRepository
import betman.pojos.Group
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class SqlGroupRepository : GroupRepository {


    override fun create(groupName: String, groupDescription: String, groupKey: String): Group {
        return transaction {
            val group: GroupDao = GroupDao.new {
                name = groupName
                description = groupDescription
                key = groupKey
            }
            commit()
            Group(group.id.value, group.name, group.description, group.key)
        }

    }
}


