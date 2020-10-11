package me.horyu.kkutuweb.oauth

enum class VendorType {
    FACEBOOK,
    GOOGLE,
    NAVER;

    companion object {
        fun fromName(vendorName: String): VendorType? {
            return VendorType.values().firstOrNull {
                it.name.equals(vendorName, ignoreCase = true)
            }
        }
    }
}