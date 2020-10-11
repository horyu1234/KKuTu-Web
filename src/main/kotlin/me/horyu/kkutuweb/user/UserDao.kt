package me.horyu.kkutuweb.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class UserDao(
        @Autowired private val jdbcTemplate: JdbcTemplate,
        @Autowired private val userMapper: UserMapper
) {
    fun getUser(id: String): User? {
        val sql = "SELECT * FROM users WHERE _id = ?"

        val users = jdbcTemplate.query(sql, arrayOf(id), userMapper)
        return if (users.isEmpty()) null else users.first()
    }

    fun updateUser(id: String, values: Map<String, Any?>) {
        val setString = values.entries.joinToString(",") {
            "${it.key}=?"
        }

        val sql = "UPDATE users SET $setString WHERE _id = ?"
        val valueString = values.map { it.value }.toMutableList()
        valueString.add(id)

        jdbcTemplate.update(sql, *valueString.toTypedArray())
    }
}