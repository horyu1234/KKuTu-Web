package me.horyu.kkutuweb.setting

data class OAuthVendorSetting(
        val order: Short,
        val clientId: String,
        val clientSecret: String,
        val callbackUrl: String,
        val button: Button
) {
    data class Button(
            val color: String,
            val backgroundColor: String
    )
}