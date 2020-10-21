package me.horyu.kkutuweb.oauth

enum class VendorType {
    FACEBOOK,
    GOOGLE,
    NAVER,
    GITHUB;

    companion object {
        fun fromName(vendorName: String): VendorType? {
            return values().firstOrNull {
                it.name.equals(vendorName, ignoreCase = true)
            }
        }
    }
}