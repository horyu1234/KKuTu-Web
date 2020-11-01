package me.horyu.kkutuweb

import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class MobileControllerAdvice {
    @ModelAttribute("mobile")
    fun isMobile(request: HttpServletRequest): Boolean {
        val userAgent = request.getHeader(HttpHeaders.USER_AGENT)
        return userAgent != null && userAgent.toLowerCase().contains("mobile")
    }
}