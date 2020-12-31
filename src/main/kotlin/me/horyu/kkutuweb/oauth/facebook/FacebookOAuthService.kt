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

package me.horyu.kkutuweb.oauth.facebook

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.scribejava.apis.FacebookApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth20Service
import me.horyu.kkutuweb.SessionAttribute
import me.horyu.kkutuweb.extension.setOAuthUser
import me.horyu.kkutuweb.oauth.Gender
import me.horyu.kkutuweb.oauth.OAuthService
import me.horyu.kkutuweb.oauth.OAuthUser
import me.horyu.kkutuweb.oauth.VendorType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpSession

@Service
class FacebookOAuthService(
        @Autowired private val objectMapper: ObjectMapper
) : OAuthService() {
    private val logger = LoggerFactory.getLogger(FacebookOAuthService::class.java)
    private val protectedResourceUrl = "https://graph.facebook.com/v8.0/me?fields=id,name,gender,age_range"

    override fun init(apiKey: String, apiSecret: String, callbackUrl: String) {
        oAuth20Service = ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .callback(callbackUrl)
                .build(FacebookApi.instance())
    }

    override fun getAuthorizationUrl(httpSession: HttpSession): String {
        val randomState = getRandomState()
        httpSession.setAttribute(SessionAttribute.OAUTH_STATE.attributeName, randomState)
        httpSession.setAttribute(SessionAttribute.OAUTH_20_SERVICE.attributeName, oAuth20Service)

        return oAuth20Service.getAuthorizationUrl(randomState)
    }

    override fun abstractLogin(httpSession: HttpSession, oAuth20Service: OAuth20Service, code: String): Boolean {
        try {
            val accessToken = oAuth20Service.getAccessToken(code)

            val request = OAuthRequest(Verb.GET, protectedResourceUrl)
            oAuth20Service.signRequest(accessToken, request)

            val response = oAuth20Service.execute(request)
            val jsonResponse = objectMapper.readTree(response.body)

            val vendorId = jsonResponse["id"].textValue()
            val oAuthUser = OAuthUser(vendorType = VendorType.FACEBOOK,
                    vendorId = vendorId,
                    name = jsonResponse["name"].textValue(),
                    profileImage = "http://graph.facebook.com/$vendorId/picture?type=square",
                    gender = if (jsonResponse.has("gender")) Gender.fromName(jsonResponse["gender"].textValue()) else null,
                    minAge = if (jsonResponse.has("age_range") && jsonResponse["age_range"].has("min")) jsonResponse["age_range"]["min"].intValue() else null,
                    maxAge = if (jsonResponse.has("age_range") && jsonResponse["age_range"].has("max")) jsonResponse["age_range"]["max"].intValue() else null
            )

            httpSession.setAttribute(SessionAttribute.IS_GUEST.attributeName, false)
            httpSession.setOAuthUser(oAuthUser)
            return true
        } catch (e: Exception) {
            logger.error("${httpSession.id} 세션에서 로그인 중 오류가 발생했습니다.", e)
            return false
        }
    }
}