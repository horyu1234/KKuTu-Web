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

package me.horyu.kkutuweb.admin.mapper

import me.horyu.kkutuweb.admin.domain.ConnectionLog
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class ConnectionLogMapper : RowMapper<ConnectionLog> {
    override fun mapRow(rs: ResultSet, rowNum: Int): ConnectionLog {
        val id = rs.getLong("id")
        val time = rs.getTimestamp("time")
        val userId = rs.getString("user_id")
        val userName = rs.getString("user_name")
        val userIp = rs.getString("user_ip")
        val channel = rs.getInt("channel")
        val useragent = rs.getString("user_agent")
        val fingerprint2 = rs.getString("finger_print_2")

        return ConnectionLog(id, time, userId, userName, userIp, channel, useragent, fingerprint2)
    }
}