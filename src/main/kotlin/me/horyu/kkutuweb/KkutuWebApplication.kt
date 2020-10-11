package me.horyu.kkutuweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class KkutuWebApplication

fun main(args: Array<String>) {
    runApplication<KkutuWebApplication>(*args)
}
