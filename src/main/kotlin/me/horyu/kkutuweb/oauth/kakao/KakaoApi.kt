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

package me.horyu.kkutuweb.oauth.kakao

import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme

object KakaoApi : DefaultApi20() {
    override fun getAccessTokenEndpoint(): String {
        return "https://kauth.kakao.com/oauth/token"
    }

    override fun getAuthorizationBaseUrl(): String {
        return "https://kauth.kakao.com/oauth/authorize"
    }

    override fun getClientAuthentication(): ClientAuthentication {
        return RequestBodyAuthenticationScheme.instance()
    }
}