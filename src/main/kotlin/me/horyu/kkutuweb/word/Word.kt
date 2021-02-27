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

import me.horyu.kkutuweb.admin.vo.WordVO
import me.horyu.kkutuweb.utils.WordUtils
import java.util.*

data class Word(
    val id: String,
    val type: String,
    val mean: String,
    val hit: Int,
    val flag: Int,
    val theme: String
) {
    companion object {
        fun convertFrom(wordVO: WordVO): Word {
            val types = ArrayList<String>()
            val means = ArrayList<String>()
            val themes = ArrayList<String>()

            for (detail in wordVO.details) {
                types.add(detail.type.typeCode)
                means.add(detail.mean)
                themes.add(detail.theme.themeCode)
            }

            var flag = 0b000000
            val flags = wordVO.flags

            for (f in flags) {
                flag = f.flag.or(flag)
            }

            return Word(
                id = wordVO.word,
                type = types.joinToString(","),
                mean = WordUtils.serializeMean(means),
                hit = 0,
                flag = flag,
                theme = themes.joinToString(",")
            )
        }
    }
}