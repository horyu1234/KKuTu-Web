/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2021. horyu1234(admin@horyu.me)
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

package me.horyu.kkutuweb.oauth.daldalso

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import me.horyu.kkutuweb.oauth.AuthVendor
import me.horyu.kkutuweb.oauth.OAuthService
import me.horyu.kkutuweb.oauth.OAuthUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DaldalsoOAuthService(
    @Autowired private val objectMapper: ObjectMapper
) : OAuthService() {
    private val protectedResourceUrl = "https://daldal.so/oauth/api/me"

    override fun init(apiKey: String, apiSecret: String, callbackUrl: String) {
        oAuth20Service = ServiceBuilder(apiKey)
            .apiSecret(apiSecret)
            .callback(callbackUrl)
            .userAgent("KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)")
            .build(DaldalsoApi)
    }

    override fun login(code: String): OAuthUser {
        val accessToken = oAuth20Service.getAccessToken(code)

        val request = OAuthRequest(Verb.GET, protectedResourceUrl)
        oAuth20Service.signRequest(accessToken, request)

        val response = oAuth20Service.execute(request)
        val jsonResponse = objectMapper.readTree(response.body)

        val profileImage = jsonResponse["profile"]["image"].textValue()
        val fixedProfileImage =
            if (profileImage == "https://daldal.so/anonymous.png") "https://daldal.so/media/images/anonymous.png" else profileImage

        return OAuthUser(
            authVendor = AuthVendor.DALDALSO,
            vendorId = jsonResponse["key"].textValue(),
            name = jsonResponse["name"].textValue(),
            profileImage = fixedProfileImage,
            gender = null,
            minAge = null,
            maxAge = null
        )
    }
}