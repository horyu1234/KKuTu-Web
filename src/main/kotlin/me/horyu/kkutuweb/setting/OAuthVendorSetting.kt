package me.horyu.kkutuweb.setting

data class OAuthVendorSetting(
        val order: Short,
        val clientId: String,
        val clientSecret: String,
        val callbackUrl: String
)