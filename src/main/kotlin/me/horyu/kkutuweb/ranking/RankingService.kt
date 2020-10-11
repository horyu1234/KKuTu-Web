package me.horyu.kkutuweb.ranking

import me.horyu.kkutuweb.ranking.response.RankResponse
import me.horyu.kkutuweb.ranking.response.ResponseRank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RankingService(
        @Autowired private val rankDao: RankDao
) {
    fun getRanking(p: Long?, id: String?): RankResponse {
        return if (id == null) {
            val page = p ?: 0L
            val ranks = rankDao.getPage(page, 15)
            RankResponse(page, ranks.map { ResponseRank.fromRank(it) })
        } else {
            val ranks = rankDao.getSurround(id, 15)
            RankResponse(0, ranks.map { ResponseRank.fromRank(it) })
        }
    }
}