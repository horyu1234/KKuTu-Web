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

import me.horyu.kkutuweb.block.BlockUser
import me.horyu.kkutuweb.block.database.mapper.BlockUserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class BlockUserDAO(
    @Autowired private val jdbcTemplate: JdbcTemplate,
    @Autowired private val blockUserMapper: BlockUserMapper
) {
    fun get(userId: String): BlockUser? {
        val sql = "SELECT * FROM block_user WHERE user_id = ?"

        val list = jdbcTemplate.query(sql, blockUserMapper, userId)
        return if (list.isEmpty()) null else list[0]
    }

    fun remove(id: Int) {
        val sql = "DELETE FROM block_user WHERE id = ?;"
        jdbcTemplate.update(sql, id)
    }
}