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

import me.horyu.kkutuweb.extension.getOAuthUser
import me.horyu.kkutuweb.extension.isGuest
import me.horyu.kkutuweb.oauth.OAuthService
import me.horyu.kkutuweb.oauth.VendorType
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
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

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
    @PostConstruct
    fun initOAuthServices() {
        for (entry in oAuthSetting.getSetting().entries) {
            val vendorType = entry.key
            val setting = entry.value

            getService(vendorType).init(setting.clientId, setting.clientSecret, setting.callbackUrl)
        }
    }

    fun getAuthorizationUrl(session: HttpSession, vendorType: VendorType): String? {
        return getService(vendorType).getAuthorizationUrl(session)
    }

    fun login(request: HttpServletRequest, vendorType: VendorType, code: String, state: String): Boolean {
        return getService(vendorType).login(request, code, state)
    }

    fun getSessionProfile(session: HttpSession): SessionProfile? {
        if (session.isGuest()) return null
        val oAuthUser = session.getOAuthUser()

        val userId = "${oAuthUser.vendorType.name.toLowerCase()}-${oAuthUser.vendorId}"
        val user = userDao.getUser(userId)

        val authType = oAuthUser.vendorType.name.toLowerCase()
        val title = if (user == null) oAuthUser.name else (user.nickname ?: oAuthUser.name)

        return SessionProfile(
                authType = authType,
                id = authType + "-" + oAuthUser.vendorId,
                name = oAuthUser.name,
                title = title,
                image = oAuthUser.profileImage ?: ""
        )
    }

    private fun getService(vendorType: VendorType): OAuthService {
        return when (vendorType) {
            VendorType.DALDALSO -> daldalsoOAuthService
            VendorType.FACEBOOK -> facebookOAuthService
            VendorType.GOOGLE -> googleOAuthService
            VendorType.NAVER -> naverOAuthService
            VendorType.GITHUB -> githubOAuthService
            VendorType.DISCORD -> discordOAuthService
            VendorType.KAKAO -> kakaoOAuthService
        }
    }
}