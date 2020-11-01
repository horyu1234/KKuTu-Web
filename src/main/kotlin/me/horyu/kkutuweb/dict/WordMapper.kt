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

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class WordMapper : RowMapper<Word> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Word {
        val id = rs.getString("_id")
        val type = rs.getString("type")
        val mean = rs.getString("mean")
        val hit = rs.getInt("hit")
        val flag = rs.getInt("flag")
        val theme = rs.getString("theme")

        return Word(id, type, mean, hit, flag, theme)
    }
}