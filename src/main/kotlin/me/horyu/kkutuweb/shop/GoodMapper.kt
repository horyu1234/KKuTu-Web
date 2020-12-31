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

package me.horyu.kkutuweb.shop

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class GoodMapper(
        @Autowired val objectMapper: ObjectMapper
) : RowMapper<Good> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Good {
        val id = rs.getString("_id")
        val cost = rs.getLong("cost")
        val hit = rs.getInt("hit")
        val term = rs.getInt("term")
        val group = rs.getString("group")
        val updatedAt = rs.getLong("updatedAt")
        val options = rs.getString("options")

        val jsonNode = objectMapper.readTree(options)
        return Good(id, cost, hit, term, group, updatedAt, jsonNode)
    }
}