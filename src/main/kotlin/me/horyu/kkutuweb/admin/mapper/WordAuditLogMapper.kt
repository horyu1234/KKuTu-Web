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

import me.horyu.kkutuweb.admin.domain.WordAuditLog
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class WordAuditLogMapper : RowMapper<WordAuditLog> {
    override fun mapRow(rs: ResultSet, rowNum: Int): WordAuditLog {
        return WordAuditLog(
            rs.getInt("id"),
            rs.getTimestamp("log_time").toLocalDateTime(),
            WordAuditLog.WordAuditLogType.valueOf(rs.getString("log_type")),
            rs.getString("word"),
            rs.getString("old_type"),
            rs.getString("old_mean"),
            rs.getInt("old_flag"),
            rs.getString("old_theme"),
            rs.getString("new_type"),
            rs.getString("new_mean"),
            rs.getInt("new_flag"),
            rs.getString("new_theme"),
            rs.getBoolean("update_log_ignore"),
            rs.getBoolean("update_log_include_detail"),
            rs.getString("admin")
        )
    }
}