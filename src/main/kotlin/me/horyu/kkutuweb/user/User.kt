package me.horyu.kkutuweb.user

import com.fasterxml.jackson.databind.JsonNode

data class User(
        val id: String,
        val nickname: String?,
        val money: Long,
        val kkutu: JsonNode,
        val lastLogin: Long?,
        val box: JsonNode,
        val equip: JsonNode,
        val exordial: String?,
        val black: String?,
        val server: String?,
        val password: String?,
        val friends: JsonNode
)