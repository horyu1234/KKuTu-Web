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

package me.horyu.kkutuweb.block.database.dao

import me.horyu.kkutuweb.block.BlockLog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class BlockLogDAO(
    @Autowired private val jdbcTemplate: JdbcTemplate
) {
    fun insert(blockLog: BlockLog) {
        val sql =
            "INSERT INTO block_log (log_time, log_type, block_type, user_id, case_id, ip_address, block_time, pardon_time, reason, punish_from, admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"

        jdbcTemplate.update(
            sql,
            blockLog.logTime,
            blockLog.logType.name,
            blockLog.blockType.name,
            blockLog.userId,
            blockLog.caseId,
            blockLog.ipAddress,
            blockLog.blockTime,
            blockLog.pardonTime,
            blockLog.reason,
            blockLog.punishFrom.name,
            blockLog.admin
        )
    }
}