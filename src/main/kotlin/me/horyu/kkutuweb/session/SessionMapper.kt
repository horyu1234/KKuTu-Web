package me.horyu.kkutuweb.session

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class SessionMapper(
        @Autowired val objectMapper: ObjectMapper
) : RowMapper<Session> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Session {
        val profile = rs.getString("profile")
        val id = rs.getString("_id")
        val createdAt = rs.getLong("createdAt")

        val jsonNode = objectMapper.readTree(profile)
        return Session(jsonNode, id, createdAt)
    }
}