package me.horyu.kkutuweb.result

enum class NickChangeResult(val errorCode: String) {
    INVALID_LENGTH("600"),
    INVALID_PATTERN("601"),
    HAS_BAD_WORDS("602"),
    HAS_BANNED_WORDS("603"),
    ALREADY_USING("620")
}