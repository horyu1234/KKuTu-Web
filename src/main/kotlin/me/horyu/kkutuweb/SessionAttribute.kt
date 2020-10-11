package me.horyu.kkutuweb

enum class SessionAttribute(val attributeName: String) {
    IS_GUEST("isGuest"),
    OAUTH_20_SERVICE("oAuth20Service"),
    OAUTH_STATE("oAuthState"),
    OAUTH_USER("oAuthUser")
}