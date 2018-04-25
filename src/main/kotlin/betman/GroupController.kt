package betman

import betman.pojos.NewInvite
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class GroupController {

    @GetMapping("/group/new-invite")
    fun newInvite(): NewInvite {
        val generator = RandomValueStringGenerator()
        generator.setLength(32)
        return NewInvite(generator.generate())
    }

}
