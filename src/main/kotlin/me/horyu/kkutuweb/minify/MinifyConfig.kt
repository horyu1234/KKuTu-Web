package me.horyu.kkutuweb.minify

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinifyConfig {
    @get:Bean
    val filterRegistrationBean: FilterRegistrationBean<*>
        get() = FilterRegistrationBean(MinifyFilter())
}