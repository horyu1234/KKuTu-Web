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

package me.horyu.kkutuweb.shop

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class ShopDao(
    @Autowired private val jdbcTemplate: JdbcTemplate,
    @Autowired private val shopMapper: GoodMapper
) {
    fun getGood(id: String): Good? {
        val sql = "SELECT * FROM kkutu_shop WHERE _id = ?"

        val goods = jdbcTemplate.query(sql, shopMapper, id)
        return if (goods.isEmpty()) null else goods.first()
    }

    fun getGoods(): List<Good> {
        val sql = "SELECT * FROM kkutu_shop"
        return jdbcTemplate.query(sql, shopMapper)
    }

    fun increaseHit(id: String) {
        val sql = "UPDATE kkutu_shop SET hit = hit + 1 WHERE _id = ?"
        jdbcTemplate.update(sql, id)
    }
}