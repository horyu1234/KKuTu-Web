package me.horyu.kkutuweb.oauth

import me.horyu.kkutuweb.oauth.VendorType

data class OAuthUser(val vendorType: VendorType,
                     val vendorId: String,
                     val name: String,
                     val profileImage: String,
                     val gender: Gender,
                     val minAge: Int,
                     val maxAge: Int)