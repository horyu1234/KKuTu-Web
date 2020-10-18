package me.horyu.kkutuweb.shop

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class GoodDetailMapper(
        @Autowired val objectMapper: ObjectMapper
) : RowMapper<GoodDetail> {
    override fun mapRow(rs: ResultSet, rowNum: Int): GoodDetail {
        val id = rs.getString("_id")
        val nameKoKR = rs.getString("name_ko_KR")
        val descKoKR = rs.getString("desc_ko_KR")
        val nameEnUS = rs.getString("name_en_US")
        val descEnUS = rs.getString("desc_en_US")

        return GoodDetail(id, nameKoKR, descKoKR, nameEnUS, descEnUS)
    }
}