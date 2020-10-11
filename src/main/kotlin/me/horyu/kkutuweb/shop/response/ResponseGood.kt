package me.horyu.kkutuweb.shop.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import me.horyu.kkutuweb.shop.Good

data class ResponseGood(
        @JsonProperty("_id") val id: String,
        val cost: String,
        val term: Int,
        val group: String,
        val updatedAt: String,
        val options: JsonNode
) {
    companion object {
        fun fromGood(good: Good): ResponseGood {
            return ResponseGood(good.id, good.cost.toString(), good.term, good.group, good.updatedAt.toString(), good.options)
        }
    }
}