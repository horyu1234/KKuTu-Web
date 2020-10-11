package me.horyu.kkutuweb.oauth

enum class Gender(val shortName: String) {
    MALE("M"),
    FEMALE("F"),
    OTHER("");

    companion object {
        fun fromName(genderName: String): Gender {
            return values().firstOrNull {
                it.name.equals(genderName, ignoreCase = true) || it.shortName.equals(genderName, ignoreCase = true)
            } ?: OTHER
        }
    }
}