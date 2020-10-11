package me.horyu.kkutuweb.oauth.naver

import com.google.gson.annotations.SerializedName

data class NaverResponse(
        @SerializedName("response") val realResponse: RealResponse
) {
    data class RealResponse(
            val id: String,
            val nickname: String,
            val name: String,
            val email: String,
            val gender: String,
            val age: String,
            val birthday: String,
            @SerializedName("profile_image") val profileImage: String
    )
}