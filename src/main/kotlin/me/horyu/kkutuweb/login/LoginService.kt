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

package me.horyu.kkutuweb.login

import me.horyu.kkutuweb.SessionAttribute
import me.horyu.kkutuweb.extension.*
import me.horyu.kkutuweb.oauth.AuthVendor
import me.horyu.kkutuweb.oauth.OAuthService
import me.horyu.kkutuweb.oauth.daldalso.DaldalsoOAuthService
import me.horyu.kkutuweb.oauth.discord.DiscordOAuthService
import me.horyu.kkutuweb.oauth.facebook.FacebookOAuthService
import me.horyu.kkutuweb.oauth.github.GithubOAuthService
import me.horyu.kkutuweb.oauth.google.GoogleOAuthService
import me.horyu.kkutuweb.oauth.kakao.KakaoOAuthService
import me.horyu.kkutuweb.oauth.naver.NaverOAuthService
import me.horyu.kkutuweb.session.SessionProfile
import me.horyu.kkutuweb.setting.OAuthSetting
import me.horyu.kkutuweb.user.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import kotlin.streams.asSequence

@Service
class LoginService(
    @Autowired private val oAuthSetting: OAuthSetting,
    @Autowired private val daldalsoOAuthService: DaldalsoOAuthService,
    @Autowired private val facebookOAuthService: FacebookOAuthService,
    @Autowired private val googleOAuthService: GoogleOAuthService,
    @Autowired private val naverOAuthService: NaverOAuthService,
    @Autowired private val githubOAuthService: GithubOAuthService,
    @Autowired private val discordOAuthService: DiscordOAuthService,
    @Autowired private val kakaoOAuthService: KakaoOAuthService,
    @Autowired private val userDao: UserDao
) {
    private val stateLength = 50L

    @PostConstruct
    fun initOAuthServices() {
        for (entry in oAuthSetting.getSetting().entries) {
            val vendorType = entry.key
            val setting = entry.value

            getOAuthService(vendorType).init(setting.clientId, setting.clientSecret, setting.callbackUrl)
        }
    }

    fun getAuthorizationUrl(session: HttpSession, authVendor: AuthVendor): String? {
        val randomState = getRandomState()
        session.setAttribute(SessionAttribute.OAUTH_STATE.attributeName, randomState)

        return getOAuthService(authVendor).getAuthorizationUrl(randomState)
    }

    fun login(request: HttpServletRequest, authVendor: AuthVendor, code: String, state: String): Boolean {
        return try {
            var session = request.session
            val oAuthState = session.getAttribute(SessionAttribute.OAUTH_STATE.attributeName)
                ?: return false

            if (oAuthState != state) {
                return false
            }

            try {
                session.invalidate()
                session = request.session
            } catch (e: Exception) {
            }

            val oAuthUser = getOAuthService(authVendor).login(code)

            session.removeAttribute(SessionAttribute.OAUTH_STATE)
            session.setAttribute(SessionAttribute.IS_GUEST, false)
            session.setOAuthUser(oAuthUser)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getSessionProfile(session: HttpSession): SessionProfile? {
        if (session.isGuest()) return null
        val oAuthUser = session.getOAuthUser()

        val userId = oAuthUser.getUserId()
        val user = userDao.getUser(userId)

        val authType = oAuthUser.authVendor.name.toLowerCase()
        val title = if (user == null) oAuthUser.name else (user.nickname ?: oAuthUser.name)

        return SessionProfile(
            authType = authType,
            id = authType + "-" + oAuthUser.vendorId,
            name = oAuthUser.name,
            title = title,
            image = oAuthUser.profileImage ?: ""
        )
    }

    private fun getOAuthService(authVendor: AuthVendor): OAuthService {
        return when (authVendor) {
            AuthVendor.DALDALSO -> daldalsoOAuthService
            AuthVendor.FACEBOOK -> facebookOAuthService
            AuthVendor.GOOGLE -> googleOAuthService
            AuthVendor.NAVER -> naverOAuthService
            AuthVendor.GITHUB -> githubOAuthService
            AuthVendor.DISCORD -> discordOAuthService
            AuthVendor.KAKAO -> kakaoOAuthService
        }
    }

    private fun getRandomState(): String {
        val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        return Random().ints(stateLength, 0, source.length)
            .asSequence()
            .map(source::get)
            .joinToString("")
    }
}