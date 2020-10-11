package me.horyu.kkutuweb.ranking.response

import me.horyu.kkutuweb.ranking.Rank

data class ResponseRank(
        val id: String,
        val rank: Int,
        val score: String
) {
    companion object {
        fun fromRank(rank: Rank): ResponseRank {
            return ResponseRank(rank.id, rank.rank, rank.score.toString())
        }
    }
}