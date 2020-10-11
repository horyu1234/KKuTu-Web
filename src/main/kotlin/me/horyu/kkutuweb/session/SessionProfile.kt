package me.horyu.kkutuweb.session

data class SessionProfile(
        val authType: String,
        val id: String,
        val name: String,
        val title: String,
        val image: String
)