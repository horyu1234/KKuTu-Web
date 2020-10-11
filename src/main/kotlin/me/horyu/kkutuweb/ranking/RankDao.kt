package me.horyu.kkutuweb.ranking

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import kotlin.math.max
import kotlin.math.roundToInt

@Component
class RankDao(
        @Autowired val redisTemplate: RedisTemplate<String, Any>
) {
    fun getPage(pageNum: Long, dataCount: Long): List<Rank> {
        val start = pageNum * dataCount
        val end = (pageNum + 1) * dataCount - 1

        return getRanks(start, end)
    }

    fun getSurround(id: String, dataCount: Int): List<Rank> {
        val opsForZSet = redisTemplate.opsForZSet()
        val reverseRank = opsForZSet.reverseRank("KKuTu_Score", id)!!

        val start = max(0, reverseRank - (dataCount / 2.0 + 1.0).roundToInt())
        val end = start + dataCount - 1

        return getRanks(start, end)
    }

    private fun getRanks(start: Long, end: Long): List<Rank> {
        val opsForZSet = redisTemplate.opsForZSet()
        val scores = opsForZSet.reverseRangeWithScores("KKuTu_Score", start, end)

        val ranks = ArrayList<Rank>()
        var curRank = start

        for (score in scores!!) {
            val id = score.value as String

            ranks.add(Rank(id, curRank.toInt(), score.score!!.toLong()))
            curRank++
        }

        return ranks
    }
}