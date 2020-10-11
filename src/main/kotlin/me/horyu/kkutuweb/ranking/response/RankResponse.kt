package me.horyu.kkutuweb.ranking.response

data class RankResponse(
        val page: Long,
        val data: List<ResponseRank>
)