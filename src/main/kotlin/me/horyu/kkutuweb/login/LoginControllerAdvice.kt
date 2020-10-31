package me.horyu.kkutuweb.login

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class LoginControllerAdvice(
        @Autowired private val objectMapper: ObjectMapper,
        @Autowired private val loginService: LoginService
) {
    @ModelAttribute("profile")
    fun getProfile(request: HttpServletRequest): String {
        return objectMapper.writeValueAsString(loginService.getSessionProfile(request.session))
    }
}