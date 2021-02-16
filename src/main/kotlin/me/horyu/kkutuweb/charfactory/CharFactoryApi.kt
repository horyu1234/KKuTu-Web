/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2021. horyu1234(admin@horyu.me)
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
    fun previewCharFactory(
        @PathVariable word: String,
        @RequestParam l: Int,
        @RequestParam b: String
    ): CFResult {
        return charFactoryService.previewCharFactory(word, l, b)
    }

    @PostMapping("/cf", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun charFactory(
        @RequestParam tray: String,
        session: HttpSession
    ): String {
        return charFactoryService.charFactory(tray.split("|"), session)
    }
}