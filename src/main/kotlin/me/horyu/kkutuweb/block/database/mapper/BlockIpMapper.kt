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

package me.horyu.kkutuweb.block.database.mapper

import me.horyu.kkutuweb.block.BlockIp
import me.horyu.kkutuweb.block.PunishFrom
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class BlockIpMapper : RowMapper<BlockIp> {
    override fun mapRow(rs: ResultSet, rowNum: Int): BlockIp {
        val id = rs.getInt("id")
        val ipAddress = rs.getString("ip_address")
        val time = rs.getTimestamp("time")
        val pardonTime = rs.getTimestamp("pardon_time")
        val reason = rs.getString("reason")
        val punishFrom = PunishFrom.valueOf(rs.getString("punish_from"))
        val admin = rs.getString("admin")

        return BlockIp(id, ipAddress, time, pardonTime, reason, punishFrom, admin)
    }
}