package me.horyu.kkutuweb.consume

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession

@RestController
class ConsumeApi(
        @Autowired private val consumeService: ConsumeService
) {
    @PostMapping("/consume/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun consume(@PathVariable id: String,
                session: HttpSession): String {
        return consumeService.consume(id, session)
    }
}