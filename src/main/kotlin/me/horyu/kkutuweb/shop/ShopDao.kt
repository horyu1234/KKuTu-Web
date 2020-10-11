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

        val goods = jdbcTemplate.query(sql, arrayOf(id), shopMapper)
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