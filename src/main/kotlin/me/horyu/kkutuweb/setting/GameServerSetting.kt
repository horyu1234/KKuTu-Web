package me.horyu.kkutuweb.setting

data class GameServerSetting(
        val isSecure: Boolean,
        val host: String,
        val port: Int
)