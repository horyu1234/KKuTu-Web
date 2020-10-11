package me.horyu.kkutuweb.shop

import com.fasterxml.jackson.databind.JsonNode

data class Good(
        val id: String,
        val cost: Long,
        val hit: Int,
        val term: Int,
        val group: String,
        val updatedAt: Long,
        val options: JsonNode
)