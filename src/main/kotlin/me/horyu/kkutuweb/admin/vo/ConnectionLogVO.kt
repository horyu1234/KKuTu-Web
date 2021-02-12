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

package me.horyu.kkutuweb.admin.vo

import com.fasterxml.jackson.annotation.JsonProperty
import me.horyu.kkutuweb.admin.domain.ConnectionLog
import me.horyu.kkutuweb.factory.DateFactory

data class ConnectionLogVO(
    val id: Long,
    val time: String,
    @JsonProperty("user_id") val userId: String,
    @JsonProperty("user_name") val userName: String,
    @JsonProperty("user_ip") val userIp: String,
    @JsonProperty("channel") val channel: Int,
    @JsonProperty("user_agent") val userAgent: String,
    @JsonProperty("finger_print_2") val fingerPrint2: String?,
    @JsonProperty("pcid_cookie") val pcidFromCookie: String?,
    @JsonProperty("pcid_localstorage") val pcidFromLocalStorage: String?
) {
    companion object {
        fun convertFrom(connectionLog: ConnectionLog): ConnectionLogVO {
            return ConnectionLogVO(
                id = connectionLog.id,
                time = DateFactory.DATABASE_FORMAT.format(connectionLog.time.toLocalDateTime()),
                userId = connectionLog.userId,
                userName = connectionLog.userName ?: "",
                userIp = connectionLog.userIp.replace("::ffff:", ""),
                channel = connectionLog.channel,
                userAgent = connectionLog.userAgent,
                fingerPrint2 = connectionLog.fingerPrint2,
                pcidFromCookie = connectionLog.pcidFromCookie,
                pcidFromLocalStorage = connectionLog.pcidFromLocalStorage
            )
        }
    }
}