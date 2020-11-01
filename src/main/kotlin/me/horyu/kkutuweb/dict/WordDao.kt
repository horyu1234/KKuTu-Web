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

package me.horyu.kkutuweb.dict

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class WordDao(
        @Autowired private val jdbcTemplate: JdbcTemplate,
        @Autowired private val wordMapper: WordMapper
) {
    fun getKoreanWord(id: String): Word? {
        val sql = "SELECT * FROM kkutu_ko WHERE _id = ?"

        val words = jdbcTemplate.query(sql, arrayOf(id), wordMapper)
        return if (words.isEmpty()) null else words.first()
    }

    fun getEnglishWord(id: String): Word? {
        val sql = "SELECT * FROM kkutu_en WHERE _id = ?"

        val words = jdbcTemplate.query(sql, arrayOf(id), wordMapper)
        return if (words.isEmpty()) null else words.first()
    }
}