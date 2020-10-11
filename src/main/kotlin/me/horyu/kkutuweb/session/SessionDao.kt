package me.horyu.kkutuweb.session

import com.fasterxml.jackson.databind.ObjectMapper
import org.postgresql.util.PGobject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component


@Component
class SessionDao(
        @Autowired private val jdbcTemplate: JdbcTemplate,
        @Autowired private val objectMapper: ObjectMapper,
        @Autowired private val sessionMapper: SessionMapper
) {
    fun list(): List<Session> {
        val sql = "SELECT * FROM session"

        return jdbcTemplate.query(sql, sessionMapper)
    }

    fun isExist(id: String): Boolean {
        val sql = "SELECT COUNT(*) FROM session WHERE _id = ?"

        val count = jdbcTemplate.queryForObject(sql, arrayOf(id), Int::class.java)
        return count > 0
    }

    fun insert(sessionProfile: SessionProfile, id: String) {
        val sql = "INSERT INTO session (profile, _id, \"createdAt\") VALUES (?, ?, ?)"

        val jsonbObj = PGobject()
        jsonbObj.type = "json"
        jsonbObj.value = objectMapper.writeValueAsString(sessionProfile)

        jdbcTemplate.update(sql, jsonbObj, id, System.currentTimeMillis())
    }

    fun updateNickname(userId: String, nick: String) {
        val safeNick = nick.replace("\"", "").replace("'", "")
        val sql = "UPDATE session SET profile = profile::jsonb || '{\"title\":\"$safeNick\"}' WHERE profile ->> 'id' = ?"

        jdbcTemplate.update(sql, userId)
    }

    fun remove(id: String) {
        val sql = "DELETE FROM session WHERE _id = ?"

        jdbcTemplate.update(sql, id)
    }
}