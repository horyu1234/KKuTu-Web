package me.horyu.kkutuweb.shop

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class GoodMapper(
        @Autowired val objectMapper: ObjectMapper
) : RowMapper<Good> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Good {
        val id = rs.getString("_id")
        val cost = rs.getLong("cost")
        val hit = rs.getInt("hit")
        val term = rs.getInt("term")
        val group = rs.getString("group")
        val updatedAt = rs.getLong("updatedAt")
        val options = rs.getString("options")

        val jsonNode = objectMapper.readTree(options)
        return Good(id, cost, hit, term, group, updatedAt, jsonNode)
    }
}