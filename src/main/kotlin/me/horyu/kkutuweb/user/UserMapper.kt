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

package me.horyu.kkutuweb.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserMapper(
    @Autowired val objectMapper: ObjectMapper
) : RowMapper<User> {
    override fun mapRow(rs: ResultSet, rowNum: Int): User {
        val id = rs.getString("_id")
        val nickname = rs.getString("nickname")
        val money = rs.getLong("money")
        val kkutu = rs.getString("kkutu")
        val lastLogin = rs.getLong("lastLogin")
        val box = rs.getString("box")
        val equip = rs.getString("equip")
        val exordial = rs.getString("exordial")
        val black = rs.getString("black")
        val server = rs.getString("server")
        val password = rs.getString("password")
        val friends = rs.getString("friends")

        val kkutuJson = objectMapper.readTree(kkutu)
        val boxJson = objectMapper.readTree(box ?: "{}")
        val equipJson = objectMapper.readTree(equip ?: "{}")
        val friendsJson = objectMapper.readTree(friends ?: "{}")

        return User(
            id,
            nickname,
            money,
            kkutuJson,
            lastLogin,
            boxJson,
            equipJson,
            exordial,
            black,
            server,
            password,
            friendsJson
        )
    }
}