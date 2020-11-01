/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2020. horyu1234(admin@horyu.me)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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