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

package me.horyu.kkutuweb.admin.domain

import java.time.LocalDateTime

data class WordAuditLog(
    val id: Int? = null,
    val time: LocalDateTime,
    val type: WordAuditLogType,
    val word: String,
    val oldType: String? = null,
    val oldMean: String? = null,
    val oldFlag: Int? = null,
    val oldTheme: String? = null,
    val newType: String? = null,
    val newMean: String? = null,
    val newFlag: Int? = null,
    val newTheme: String? = null,
    val admin: String
) {
    enum class WordAuditLogType {
        CREATE,
        UPDATE,
        DELETE
    }
}