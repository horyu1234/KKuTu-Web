package me.horyu.kkutuweb.dict

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class WordMapper : RowMapper<Word> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Word {
        val id = rs.getString("_id")
        val type = rs.getString("type")
        val mean = rs.getString("mean")
        val hit = rs.getInt("hit")
        val flag = rs.getInt("flag")
        val theme = rs.getString("theme")

        return Word(id, type, mean, hit, flag, theme)
    }
}