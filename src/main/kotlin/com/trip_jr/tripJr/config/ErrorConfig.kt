import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
//import javax.servlet.http.HttpServletRequest

@Controller
class CustomErrorController : ErrorController {

    @GetMapping("/error")
    fun handleError(request: HttpServletRequest): String {
        val status = request.getAttribute("javax.servlet.error.status_code") as Int
        if (status == 404) {
            return "404"
        }
        return "error"
    }

    fun getErrorPath(): String {
        return "/error"
    }
}