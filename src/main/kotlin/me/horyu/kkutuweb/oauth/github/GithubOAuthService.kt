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

package me.horyu.kkutuweb.oauth.github

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.scribejava.apis.GitHubApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import me.horyu.kkutuweb.oauth.AuthVendor
import me.horyu.kkutuweb.oauth.OAuthService
import me.horyu.kkutuweb.oauth.OAuthUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GithubOAuthService(
    @Autowired private val objectMapper: ObjectMapper
) : OAuthService() {
    private val protectedResourceUrl = "https://api.github.com/user"

    override fun init(apiKey: String, apiSecret: String, callbackUrl: String) {
        oAuth20Service = ServiceBuilder(apiKey)
            .apiSecret(apiSecret)
            .callback(callbackUrl)
            .build(GitHubApi.instance())
    }

    override fun login(code: String): OAuthUser {
        val accessToken = oAuth20Service.getAccessToken(code)

        val request = OAuthRequest(Verb.GET, protectedResourceUrl)
        oAuth20Service.signRequest(accessToken, request)

        val response = oAuth20Service.execute(request)
        val jsonResponse = objectMapper.readTree(response.body)

        return OAuthUser(
            authVendor = AuthVendor.GITHUB,
            vendorId = jsonResponse["id"].intValue().toString(),
            name = if (jsonResponse.has("name")) jsonResponse["name"].textValue() else jsonResponse["login"].textValue(),
            profileImage = jsonResponse["avatar_url"].textValue(),
            gender = null,
            minAge = null,
            maxAge = null
        )
    }
}