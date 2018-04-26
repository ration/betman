package betman

<<<<<<< HEAD
import betman.pojos.NewInvite
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
=======
import betman.db.GroupRepository
import betman.pojos.Group
import betman.pojos.NewInvite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
>>>>>>> c787112ed78de5ace1975bf9d1e26fe7aa49e95d
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
<<<<<<< HEAD
class GroupController {

    @GetMapping("/group/new-invite")
    fun newInvite(): NewInvite {
        val generator = RandomValueStringGenerator()
        generator.setLength(32)
        return NewInvite(generator.generate())
=======
class GroupController @Autowired constructor(private val repository: GroupRepository) {

    @GetMapping("/group/new",  produces = [MediaType.APPLICATION_JSON_VALUE])
    fun newInvite(@RequestParam name: String,
                  @RequestParam description: String): Group {
        return repository.create(name,description)
>>>>>>> c787112ed78de5ace1975bf9d1e26fe7aa49e95d
    }

}
