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

package me.horyu.kkutuweb.admin.dao

import me.horyu.kkutuweb.admin.domain.WordAuditLog
import me.horyu.kkutuweb.extension.toTimestamp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class WordAuditLogDAO(
    @Autowired private val jdbcTemplate: JdbcTemplate
) {
    fun insert(lang: String, wordAuditLog: WordAuditLog) {
        val sql =
            "INSERT INTO kkutu_${lang}_audit_log (log_time, log_type, word, old_type, old_mean, old_flag, old_theme, new_type, new_mean, new_flag, new_theme, update_log_ignore, update_log_include_detail, admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"

        jdbcTemplate.update(
            sql,
            wordAuditLog.time.toTimestamp(),
            wordAuditLog.type.name,
            wordAuditLog.word,
            wordAuditLog.oldType,
            wordAuditLog.oldMean,
            wordAuditLog.oldFlag,
            wordAuditLog.oldTheme,
            wordAuditLog.newType,
            wordAuditLog.newMean,
            wordAuditLog.newFlag,
            wordAuditLog.newTheme,
            wordAuditLog.updateLogIgnore,
            wordAuditLog.updateLogIncludeDetail,
            wordAuditLog.admin
        )
    }
}
