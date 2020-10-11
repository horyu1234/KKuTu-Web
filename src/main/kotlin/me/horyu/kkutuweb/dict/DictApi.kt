package me.horyu.kkutuweb.dict

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DictApi(
        @Autowired private val dictService: DictService
) {
    @GetMapping("/dict/{word}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getWord(@PathVariable word: String,
                @RequestParam lang: String): String {
        return dictService.getWord(word, lang)
    }
}