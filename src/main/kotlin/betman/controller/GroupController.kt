package betman.controller

import betman.db.GroupRepository
import betman.pojos.Group
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator
import org.springframework.web.bind.annotation.*
import java.security.Principal


@RestController
@RequestMapping("/api/groups")
class GroupController @Autowired constructor(private val repository: GroupRepository) {

    @PostMapping("/new", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun new(@RequestBody group: Group): Group {
        return repository.create(group, generateKey())
    }

    @PostMapping("join")
    fun join(@RequestParam("key") key: String, @RequestParam displayName: String,
             principal: Principal): Group {
        return repository.join(key, displayName, principal.name)
    }

    private fun generateKey(): String {
        val generator = RandomValueStringGenerator()
        generator.setLength(32)
        return generator.generate()
    }
}
