package me.horyu.kkutuweb.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@RestController
class UserApi(
        @Autowired private val userService: UserService
) {
    @GetMapping("/box", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBox(session: HttpSession): String {
        return userService.getBox(session)
    }

    @PostMapping("/exordial", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun exordial(@RequestParam data: String,
                 @RequestParam nick: String,
                 session: HttpSession): String {
        return userService.exordial(data, nick, session)
    }

    @PostMapping("/equip/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun equip(@PathVariable id: String,
              @RequestParam(required = false, defaultValue = "false") isLeft: Boolean,
              session: HttpSession): String {
        return userService.equip(id, isLeft, session)
    }
}