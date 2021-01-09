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

package me.horyu.kkutuweb.block

import me.horyu.kkutuiodiscordbot.domain.BlockType
import me.horyu.kkutuiodiscordbot.domain.LogType
import me.horyu.kkutuweb.extension.toTimestamp
import java.sql.Timestamp
import java.time.LocalDateTime

data class BlockLog(
    val id: Int = 0,
    val logTime: Timestamp,
    val logType: LogType,
    val blockType: BlockType,
    val caseId: Int? = null,
    val userId: String? = null,
    val ipAddress: String? = null,
    val blockTime: Timestamp,
    val pardonTime: Timestamp? = null,
    val reason: String,
    val punishFrom: PunishFrom,
    val admin: String
) {
    companion object {
        fun fromAddOf(blockUser: BlockUser, logType: LogType = LogType.ADD): BlockLog {
            return BlockLog(
                0,
                LocalDateTime.now().toTimestamp(),
                logType,
                BlockType.USER,
                blockUser.id,
                blockUser.userId,
                null,
                blockUser.time,
                blockUser.pardonTime,
                blockUser.reason,
                blockUser.punishFrom,
                blockUser.admin
            )
        }

        fun fromAddOf(blockIp: BlockIp, logType: LogType = LogType.ADD): BlockLog {
            return BlockLog(
                0,
                LocalDateTime.now().toTimestamp(),
                logType,
                BlockType.IP,
                blockIp.id,
                null,
                blockIp.ipAddress,
                blockIp.time,
                blockIp.pardonTime,
                blockIp.reason,
                blockIp.punishFrom,
                blockIp.admin
            )
        }

        fun fromAddOf(blockChat: BlockChat, logType: LogType = LogType.ADD): BlockLog {
            return BlockLog(
                0,
                LocalDateTime.now().toTimestamp(),
                logType,
                BlockType.CHAT,
                blockChat.id,
                blockChat.userId,
                null,
                blockChat.time,
                blockChat.pardonTime,
                blockChat.reason,
                blockChat.punishFrom,
                blockChat.admin
            )
        }
    }
}