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

package me.horyu.kkutuweb.oauth.discord

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.scribejava.apis.DiscordApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth20Service
import me.horyu.kkutuweb.SessionAttribute
import me.horyu.kkutuweb.oauth.OAuthService
import me.horyu.kkutuweb.oauth.OAuthUser
import me.horyu.kkutuweb.oauth.VendorType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpSession


@Service
class DiscordOAuthService(
        @Autowired private val objectMapper: ObjectMapper
) : OAuthService() {
    private val logger = LoggerFactory.getLogger(DiscordOAuthService::class.java)
    private val protectedResourceUrl = "https://discordapp.com/api/users/@me"

    override fun init(apiKey: String, apiSecret: String, callbackUrl: String) {
        oAuth20Service = ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .callback(callbackUrl)
                .defaultScope("identify")
                .userAgent("KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)")
                .build(DiscordApi.instance())
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

            val oAuthUser = OAuthUser(vendorType = VendorType.DISCORD,
                    vendorId = jsonResponse["id"].textValue(),
                    name = jsonResponse["username"].textValue() + "#" + jsonResponse["discriminator"].textValue(),
                    profileImage = "https://cdn.discordapp.com/avatars/${jsonResponse["id"].longValue()}/${jsonResponse["avatar"].textValue()}",
                    gender = null,
                    minAge = null,
                    maxAge = null
            )

            httpSession.setAttribute(SessionAttribute.IS_GUEST.attributeName, false)
            httpSession.setAttribute(SessionAttribute.OAUTH_USER.attributeName, oAuthUser)
            return true
        } catch (exception: Exception) {
            exception.printStackTrace()
            logger.warn("${httpSession.id} 세션에서 로그인에 실패하였습니다. ${exception.message}")
            return false
        }
    }
}