package me.horyu.kkutuweb.locale

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import org.springframework.web.servlet.support.RequestContextUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class LocaleInterceptor(
        @Autowired private val localePropertyLoader: LocalePropertyLoader
) : HandlerInterceptorAdapter() {
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        if (modelAndView == null) return

        val locale = RequestContextUtils.getLocale(request)
        val messages = localePropertyLoader.getMessages(locale)

        modelAndView.model["language"] = locale.language
        modelAndView.model["messages"] = messages
    }
}