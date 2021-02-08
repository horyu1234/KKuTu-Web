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

package me.horyu.kkutuweb.word

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.common.base.Strings

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class WordFlag(val flag: Int, val flagName: String) {
    NONE(0b000000, "일반"),
    LOANWORD(0b000001, "외래어"),
    INJEONG(0b000010, "어인정"),
    SPACED(0b000100, "띄어쓰기를 해야 하는 어휘"),
    SATURI(0b001000, "방언"),
    OLD(0b010000, "옛말"),
    MUNHWA(0b100000, "문화어");

    @JsonIgnore
    val flagString = "0b" + Strings.padStart(Integer.toBinaryString(flag), 6, '0')

    companion object {
        fun findByFlag(flag: Int): WordFlag? {
            for (wordFlag in values()) {
                if (wordFlag.flag == flag) {
                    return wordFlag
                }
            }
            return null
        }

        fun findByName(name: String): WordFlag? {
            for (wordFlag in values()) {
                if (wordFlag.name == name) {
                    return wordFlag
                }
            }
            return null
        }
    }
}