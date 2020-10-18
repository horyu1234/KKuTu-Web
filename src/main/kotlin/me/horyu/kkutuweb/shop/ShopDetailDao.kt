package me.horyu.kkutuweb.shop

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class ShopDetailDao(
        @Autowired private val jdbcTemplate: JdbcTemplate,
        @Autowired private val shopDetailMapper: GoodDetailMapper
) {
    fun getGoodDetails(): List<GoodDetail> {
        val sql = "SELECT * FROM kkutu_shop_desc"
        return jdbcTemplate.query(sql, shopDetailMapper)
    }
}