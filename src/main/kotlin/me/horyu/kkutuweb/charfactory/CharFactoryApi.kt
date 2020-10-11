package me.horyu.kkutuweb.charfactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@RestController
class CharFactoryApi(
        @Autowired private val charFactoryService: CharFactoryService
) {
    @GetMapping("/cf/{word}")
    fun previewCharFactory(@PathVariable word: String,
                           @RequestParam l: Int,
                           @RequestParam b: String,
                           session: HttpSession): CFResult {
        return charFactoryService.previewCharFactory(word, l, b, session)
    }

    @PostMapping("/cf", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun charFactory(@RequestParam tray: String,
                    session: HttpSession): String {
        return charFactoryService.charFactory(tray.split("|"), session)
    }
}