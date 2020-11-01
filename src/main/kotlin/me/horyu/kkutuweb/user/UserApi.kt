/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2020. horyu1234(admin@horyu.me)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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