package betman

import betman.db.GroupRepository
import betman.pojos.Group
import betman.pojos.NewInvite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class GroupController @Autowired constructor(private val repository: GroupRepository) {

    @GetMapping("/group/new",  produces = [MediaType.APPLICATION_JSON_VALUE])
    fun newInvite(@RequestParam name: String,
                  @RequestParam description: String): Group {
        return repository.create(name,description)
    }

}
