package betman

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView


/**
 * This is for Angular routing urls
 */
@RestController
@RequestMapping
class ForwardController {

    @GetMapping(value = ["/**/{[path:[^\\.]*}"])
    fun forward(): ModelAndView {
        return ModelAndView("/index.html")
    }
}