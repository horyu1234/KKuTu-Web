package me.horyu.kkutuweb.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .headers().frameOptions().disable()
            .and()
            .authorizeRequests {
                it
                    .antMatchers("/actuator/**").authenticated()
                    .anyRequest().permitAll()
            }
            .httpBasic(Customizer.withDefaults())
            .csrf().disable()
    }
}