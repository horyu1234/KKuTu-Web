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

package me.horyu.kkutuweb.oauth.naver

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.scribejava.apis.NaverApi
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
class NaverOAuthService(
        @Autowired private val objectMapper: ObjectMapper
) : OAuthService() {
    private val logger = LoggerFactory.getLogger(NaverOAuthService::class.java)
    private val protectedResourceUrl = "https://openapi.naver.com/v1/nid/me"

    override fun init(apiKey: String, apiSecret: String, callbackUrl: String) {
        oAuth20Service = ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .callback(callbackUrl)
                .build(NaverApi.instance())
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

            val responseNode = jsonResponse["response"]
            val splitAge = responseNode["age"].textValue().split("-")
            val oAuthUser = OAuthUser(vendorType = VendorType.NAVER,
                    vendorId = responseNode["id"].textValue(),
                    name = responseNode["nickname"].textValue(),
                    profileImage = responseNode["profile_image"].textValue(),
                    gender = Gender.fromName(responseNode["gender"].textValue()),
                    minAge = splitAge[0].toIntOrNull(),
                    maxAge = splitAge[1].toIntOrNull()
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