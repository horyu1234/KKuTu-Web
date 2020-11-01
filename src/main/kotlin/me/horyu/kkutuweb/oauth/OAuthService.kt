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

package me.horyu.kkutuweb.oauth

import com.github.scribejava.core.oauth.OAuth20Service
import me.horyu.kkutuweb.SessionAttribute
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import kotlin.streams.asSequence

abstract class OAuthService {
    private val stateLength = 50L

    protected lateinit var oAuth20Service: OAuth20Service

    abstract fun init(apiKey: String, apiSecret: String, callbackUrl: String)
    abstract fun getAuthorizationUrl(httpSession: HttpSession): String
    abstract fun abstractLogin(httpSession: HttpSession, oAuth20Service: OAuth20Service, code: String): Boolean

    fun login(request: HttpServletRequest, code: String, state: String): Boolean {
        var session = request.session
        val oAuthState = session.getAttribute(SessionAttribute.OAUTH_STATE.attributeName)
                ?: return false
        val oAuthServiceSession = session.getAttribute(SessionAttribute.OAUTH_20_SERVICE.attributeName)
                ?: return false

        if (oAuthState != state) {
            return false
        }

        try {
            session.invalidate()
            session = request.session
        } catch (e: Exception) {
        }

        val oAuthService = oAuthServiceSession as OAuth20Service
        return abstractLogin(session, oAuthService, code)
    }

    protected fun getRandomState(): String {
        val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        return Random().ints(stateLength, 0, source.length)
                .asSequence()
                .map(source::get)
                .joinToString("")
    }
}