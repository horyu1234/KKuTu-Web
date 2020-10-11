package me.horyu.kkutuweb.oauth.facebook

import com.google.gson.annotations.SerializedName

data class FacebookResponse(
        val id: String,
        val name: String,
        val gender: String,
        @SerializedName("age_range") val ageRange: AgeRange
) {
    fun getProfileImage(): String {
        return "http://graph.facebook.com/$id/picture?type=square"
    }

    data class AgeRange(val min: Int,
                        val max: Int)
}