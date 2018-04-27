package betman.db.sql

import betman.pojos.Group
import org.junit.Assert.assertNotNull
import org.junit.Test

class SqlGroupRepositoryTest : DbTest() {

    @Test
    fun create() {
        val name = "name"
        val description = "description"
        val key = "key"
        val repository = SqlGroupRepository()
        val groups = repository.create(Group(name=name, description = description, key = key))
        assertNotNull(groups.id)
    }


}