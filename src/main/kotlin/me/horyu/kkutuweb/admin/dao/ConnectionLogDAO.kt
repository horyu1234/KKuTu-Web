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

import me.horyu.kkutuweb.admin.SortType
import me.horyu.kkutuweb.admin.domain.ConnectionLog
import me.horyu.kkutuweb.admin.mapper.ConnectionLogMapper
import me.horyu.kkutuweb.admin.mapper.SingleNumberMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ConnectionLogDAO(
    @Autowired private val jdbcTemplate: JdbcTemplate,
    @Autowired private val connectionLogMapper: ConnectionLogMapper,
    @Autowired private val singleNumberMapper: SingleNumberMapper
) {
    fun getDataCount(): Int {
        val sql = "SELECT COUNT(*) FROM connection_log"

        val list = jdbcTemplate.query(sql, singleNumberMapper)
        return list[0]
    }

    fun getPageData(
        page: Int,
        pageSize: Int,
        sortField: String,
        sortType: SortType,
        searchFilters: Map<String, String>
    ): List<ConnectionLog> {
        val whereQueryParts = ArrayList<String>()
        for (key in searchFilters.keys) {
            whereQueryParts.add("CAST($key AS TEXT) ILIKE ?")
        }

        val whereQuery = if (whereQueryParts.isEmpty()) "" else "WHERE " + whereQueryParts.joinToString(" AND ")
        val whereValues = searchFilters.values.map { "%${it}%" }.toTypedArray()

        val sql =
            "SELECT * FROM connection_log $whereQuery ORDER BY $sortField ${sortType.name} LIMIT $pageSize OFFSET ${page * pageSize}"

        return jdbcTemplate.query(sql, connectionLogMapper, *whereValues)
    }
}
