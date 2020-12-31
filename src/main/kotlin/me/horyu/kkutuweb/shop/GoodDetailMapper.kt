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
class GoodDetailMapper(
        @Autowired val objectMapper: ObjectMapper
) : RowMapper<GoodDetail> {
    override fun mapRow(rs: ResultSet, rowNum: Int): GoodDetail {
        val id = rs.getString("_id")
        val nameKoKR = rs.getString("name_ko_KR")
        val descKoKR = rs.getString("desc_ko_KR")
        val nameEnUS = rs.getString("name_en_US")
        val descEnUS = rs.getString("desc_en_US")

        return GoodDetail(id, nameKoKR, descKoKR, nameEnUS, descEnUS)
    }
}