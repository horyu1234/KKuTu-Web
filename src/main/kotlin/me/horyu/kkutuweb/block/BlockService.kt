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
import me.horyu.kkutuweb.block.database.dao.BlockIpDAO
import me.horyu.kkutuweb.block.database.dao.BlockLogDAO
import me.horyu.kkutuweb.block.database.dao.BlockUserDAO
import me.horyu.kkutuweb.extension.getIp
import me.horyu.kkutuweb.extension.getOAuthUser
import me.horyu.kkutuweb.extension.isGuest
import me.horyu.kkutuweb.extension.toTimestamp
import me.horyu.kkutuweb.factory.DateFactory
import me.horyu.kkutuweb.utils.TimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@Service
class BlockService(
    @Autowired private val blockUserDAO: BlockUserDAO,
    @Autowired private val blockIpDAO: BlockIpDAO,
    @Autowired private val blockLogDAO: BlockLogDAO
) {
    fun getBlockStatus(request: HttpServletRequest): BlockStatus {
        val session = request.session
        val ip = request.getIp()

        val blockIp = getBlockIp(ip)
        if (blockIp != null) {
            return BlockStatus(
                blocked = true,
                blockType = BlockType.IP,
                target = ip,
                id = blockIp.id,
                time = DateFactory.PRETTY_FORMAT.format(blockIp.time.toLocalDateTime()),
                pardonTime = if (blockIp.pardonTime == null) null else DateFactory.PRETTY_FORMAT.format(blockIp.pardonTime.toLocalDateTime()),
                duration = if (blockIp.pardonTime == null) "영구 정지" else TimeUtils.getTimeTextForSeconds(
                    getDurationSeconds(blockIp.pardonTime, blockIp.time)
                ),
                remain = if (blockIp.pardonTime == null) "영구 정지" else TimeUtils.getTimeTextForSeconds(
                    getDurationSeconds(blockIp.pardonTime, LocalDateTime.now().toTimestamp())
                ),
                reason = blockIp.reason
            )
        }

        if (!session.isGuest()) {
            val oAuthUser = session.getOAuthUser()
            val userId = oAuthUser.getUserId()

            val blockUser = getBlockUser(userId)
            if (blockUser != null) {
                return BlockStatus(
                    blocked = true,
                    blockType = BlockType.USER,
                    target = userId,
                    id = blockUser.id,
                    time = DateFactory.PRETTY_FORMAT.format(blockUser.time.toLocalDateTime()),
                    pardonTime = if (blockUser.pardonTime == null) null else DateFactory.PRETTY_FORMAT.format(blockUser.pardonTime.toLocalDateTime()),
                    duration = if (blockUser.pardonTime == null) "영구 정지" else TimeUtils.getTimeTextForSeconds(
                        getDurationSeconds(blockUser.pardonTime, blockUser.time)
                    ),
                    remain = if (blockUser.pardonTime == null) "영구 정지" else TimeUtils.getTimeTextForSeconds(
                        getDurationSeconds(blockUser.pardonTime, LocalDateTime.now().toTimestamp())
                    ),
                    reason = blockUser.reason
                )
            }
        }

        return BlockStatus()
    }

    private fun getDurationSeconds(big: Timestamp, small: Timestamp): Long {
        return (big.time - small.time) / 1000
    }

    private fun getBlockUser(id: String): BlockUser? {
        val blockUser = blockUserDAO.get(id) ?: return null
        if (blockUser.pardonTime == null) return blockUser

        if (blockUser.pardonTime.before(LocalDateTime.now().toTimestamp())) {
            blockUserDAO.remove(blockUser.id)
            blockLogDAO.insert(BlockLog.fromAddOf(blockUser, LogType.AUTO_REMOVE))
            return null
        }

        return blockUser
    }

    private fun getBlockIp(ip: String): BlockIp? {
        val blockIp = blockIpDAO.get(ip) ?: return null
        if (blockIp.pardonTime == null) return blockIp

        if (blockIp.pardonTime.before(LocalDateTime.now().toTimestamp())) {
            blockIpDAO.remove(blockIp.id)
            blockLogDAO.insert(BlockLog.fromAddOf(blockIp, LogType.AUTO_REMOVE))
            return null
        }

        return blockIp
    }
}