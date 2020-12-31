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

import org.postgresql.util.PGobject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SingleColumnRowMapper
import org.springframework.stereotype.Component

@Component
class UserDao(
    @Autowired private val jdbcTemplate: JdbcTemplate,
    @Autowired private val userMapper: UserMapper
) {
    fun getUser(id: String): User? {
        val sql = "SELECT * FROM users WHERE _id = ?"

        val users = jdbcTemplate.query(sql, arrayOf(id), userMapper)
        return if (users.isEmpty()) null else users.first()
    }

    fun getSimilarityNicks(): List<String> {
        val sql = "SELECT \"meanableNick\" FROM users"
        return jdbcTemplate.query(sql, SingleColumnRowMapper())
    }

    fun newUser(id: String, nick: String, similarityNick: String) {
        val sql = "INSERT INTO users(_id, nickname, money, kkutu, \"meanableNick\") VALUES(?, ?, ?, ?, ?)"

        val kkutuJsonObj = PGobject()
        kkutuJsonObj.type = "json"
        kkutuJsonObj.value =
            "{\"score\":0,\"playTime\":0,\"connectDate\":0,\"record\":{\"EKT\":[0,0,0,0],\"ESH\":[0,0,0,0],\"KKT\":[0,0,0,0],\"KSH\":[0,0,0,0],\"CSQ\":[0,0,0,0],\"KCW\":[0,0,0,0],\"KTY\":[0,0,0,0],\"ETY\":[0,0,0,0],\"KAP\":[0,0,0,0],\"HUN\":[0,0,0,0],\"KDA\":[0,0,0,0],\"EDA\":[0,0,0,0],\"KSS\":[0,0,0,0],\"ESS\":[0,0,0,0]}}"

        jdbcTemplate.update(sql, id, nick, 0, kkutuJsonObj, similarityNick)
    }

    fun updateUser(id: String, values: Map<String, Any?>) {
        val setString = values.entries.joinToString(",") {
            "${it.key}=?"
        }

        val sql = "UPDATE users SET $setString WHERE _id = ?"
        val valueString = values.map { it.value }.toMutableList()
        valueString.add(id)

        jdbcTemplate.update(sql, *valueString.toTypedArray())
    }
}