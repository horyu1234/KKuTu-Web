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

package me.horyu.kkutuweb.admin.vo

import me.horyu.kkutuweb.utils.WordUtils
import me.horyu.kkutuweb.word.Word
import me.horyu.kkutuweb.word.WordFlag
import me.horyu.kkutuweb.word.WordTheme
import me.horyu.kkutuweb.word.WordType
import org.slf4j.LoggerFactory
import java.util.*

data class WordVO(
    val word: String,
    val hit: Int,
    val flags: List<WordFlag>,
    val details: List<WordDetailVO>
) {
    companion object {
        private val logger = LoggerFactory.getLogger(WordVO::class.java)

        fun convertFrom(word: Word): WordVO {
            val types = word.type.split(",")
            val themes = word.theme.split(",")
            val means = WordUtils.deserializeMean(word.mean)

            val details = ArrayList<WordDetailVO>()
            for (i in types.indices) {
                val typeCode = types[i]
                val themeCode = themes[i]

                val type = WordType.findByCode(typeCode)
                val theme = WordTheme.findByCode(themeCode)
                if (type == null) {
                    logger.warn("데이터베이스에 저장된 단어 유형에 따른 정보를 찾을 수 없습니다. 유형 코드: $typeCode")
                    continue
                }

                if (theme == null) {
                    logger.warn("데이터베이스에 저장된 단어 주제에 따른 정보를 찾을 수 없습니다. 주제 코드: $themeCode")
                    continue
                }

                val mean = if (means.size > i) means[i] else ""
                details.add(WordDetailVO(type, mean.trim(), theme))
            }

            val flag = word.flag
            val flags = ArrayList<WordFlag>()

            for (wordFlag in WordFlag.values()) {
                if (flag.and(wordFlag.flag) == 0) continue
                flags.add(wordFlag)
            }

            return WordVO(
                word = word.id,
                hit = word.hit,
                flags = flags,
                details = details
            )
        }
    }
}