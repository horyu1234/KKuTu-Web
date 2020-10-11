package me.horyu.kkutuweb.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserMapper(
        @Autowired val objectMapper: ObjectMapper
) : RowMapper<User> {
    override fun mapRow(rs: ResultSet, rowNum: Int): User {
        val id = rs.getString("_id")
        val nickname = rs.getString("nickname")
        val money = rs.getLong("money")
        val kkutu = rs.getString("kkutu")
        val lastLogin = rs.getLong("lastLogin")
        val box = rs.getString("box")
        val equip = rs.getString("equip")
        val exordial = rs.getString("exordial")
        val black = rs.getString("black")
        val server = rs.getString("server")
        val password = rs.getString("password")
        val friends = rs.getString("friends")

        val kkutuJson = objectMapper.readTree(kkutu)
        val boxJson = objectMapper.readTree(box ?: "{}")
        val equipJson = objectMapper.readTree(equip ?: "{}")
        val friendsJson = objectMapper.readTree(friends ?: "{}")

        return User(id, nickname, money, kkutuJson, lastLogin, boxJson, equipJson, exordial, black, server, password, friendsJson)
    }
}