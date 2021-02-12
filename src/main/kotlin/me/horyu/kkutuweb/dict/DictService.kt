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

package me.horyu.kkutuweb.dict

import me.horyu.kkutuweb.word.WordDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DictService(
    @Autowired private val wordDao: WordDao
) {
    fun getWord(id: String, lang: String): String {
        val tableName = when (lang) {
            "ko" -> "kkutu_ko"
            "en" -> "kkutu_en"
            else -> ""
        }

        if (tableName.isEmpty()) return "{\"error\":400}"
        val words = wordDao.getWords(tableName, id)
        if (words.isEmpty()) return "{\"error\":404}"

        val word = words.first()
        return "{\"word\":\"${word.id}\",\"mean\":\"${
            word.mean.replace(
                "\"",
                "\\\""
            )
        }\",\"theme\":\"${word.theme}\",\"type\":\"${word.type}\"}"
    }
}
