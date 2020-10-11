package me.horyu.kkutuweb.dict

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class WordDao(
        @Autowired private val jdbcTemplate: JdbcTemplate,
        @Autowired private val wordMapper: WordMapper
) {
    fun getKoreanWord(id: String): Word? {
        val sql = "SELECT * FROM kkutu_ko WHERE _id = ?"

        val words = jdbcTemplate.query(sql, arrayOf(id), wordMapper)
        return if (words.isEmpty()) null else words.first()
    }

    fun getEnglishWord(id: String): Word? {
        val sql = "SELECT * FROM kkutu_en WHERE _id = ?"

        val words = jdbcTemplate.query(sql, arrayOf(id), wordMapper)
        return if (words.isEmpty()) null else words.first()
    }
}