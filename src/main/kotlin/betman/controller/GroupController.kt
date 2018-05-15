package betman.controller

import betman.db.GroupRepository
import betman.pojos.Group
import betman.pojos.Groups
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator
import org.springframework.web.bind.annotation.*
import java.security.Principal


@RestController
@RequestMapping("/api/groups")
class GroupController @Autowired constructor(private val repository: GroupRepository) {

    @PostMapping("/new", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun new(@RequestBody group: Group, principal: Principal): Single<Group> {
        return repository.create(group, generateKey(), principal.name).flatMap {
            repository.join(it.key!!, principal.name, principal.name)
        }
    }

    @PostMapping("/join")
    fun join(@RequestParam("key") key: String, @RequestParam displayName: String,
             principal: Principal): Single<Group> {
        return repository.join(key, principal.name, displayName)
    }


    @PostMapping("/updateDisplayName")
    fun updateDisplayName(@RequestParam groupKey: String, @RequestParam displayName: String, principal: Principal) {
        return repository.updateDisplayName(groupKey, principal.name, displayName)
    }

    @GetMapping("/")
    fun get(principal: Principal): Single<Groups> {
        return repository.get(principal.name).map { Groups(it) }
    }

    @GetMapping("/{key}")
    fun get(@PathVariable key: String, principal: Principal): Maybe<Group> {
        return repository.get(key, principal.name)
    }


    private fun generateKey(): String {
        val generator = RandomValueStringGenerator()
        generator.setLength(32)
        return generator.generate()
    }

    @PostMapping("/update")
    fun update(@RequestBody group: Group, principal: Principal): Completable {
        return repository.update(group, principal.name)
    }

}
