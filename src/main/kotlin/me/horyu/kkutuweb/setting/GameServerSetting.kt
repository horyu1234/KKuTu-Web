package me.horyu.kkutuweb.setting

data class GameServerSetting(
        val isSecure: Boolean,
        val publicHost: String,
        val host: String,
        val port: Int,
        val cid: Short
)