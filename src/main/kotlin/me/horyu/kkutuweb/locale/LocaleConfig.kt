package me.horyu.kkutuweb.locale

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import java.util.*

@Configuration
class LocaleConfig : WebMvcConfigurer {
    @Bean
    fun localeResolver(): LocaleResolver {
        val localeResolver = CookieLocaleResolver()
        localeResolver.cookieName = "user.language"
        localeResolver.setDefaultLocale(Locale.KOREAN)
        return localeResolver
    }
}