package me.horyu.kkutuweb.session

import com.fasterxml.jackson.databind.JsonNode

data class Session(
        val profile: JsonNode,
        val id: String,
        val createdAt: Long
)