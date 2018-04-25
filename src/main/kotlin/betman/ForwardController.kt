package betman

import org.springframework.web.servlet.ModelAndView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class ForwardController {

    @GetMapping(value = "/**/{[path:[^\\.]*}")
    fun forward(): ModelAndView {
        return ModelAndView("/index.html")
    }
}