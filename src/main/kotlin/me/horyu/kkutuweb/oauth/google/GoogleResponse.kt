package me.horyu.kkutuweb.oauth.google

data class GoogleResponse(
        val id: String,
        val nickname: String,
        val displayName: String,
        val image: Image,
        val gender: String?,
        val ageRange: AgeRange?
) {
    data class Image(val url: String)

    data class AgeRange(val min: Int,
                        val max: Int)
}