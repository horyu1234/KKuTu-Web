package me.horyu.kkutuweb.login

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class LoginInterceptor(
        @Autowired private val objectMapper: ObjectMapper,
        @Autowired private val loginService: LoginService
) : HandlerInterceptorAdapter() {
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        if (modelAndView == null) return

        val sessionProfile = loginService.getSessionProfile(request.session)
        modelAndView.model["profile"] = objectMapper.writeValueAsString(sessionProfile)
    }
}