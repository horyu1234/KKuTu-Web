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
import me.horyu.kkutuweb.oauth.naver.NaverOAuthService
import me.horyu.kkutuweb.session.SessionProfile
import me.horyu.kkutuweb.user.UserDao
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Service
class LoginService(
        @Autowired private val daldalsoOAuthService: DaldalsoOAuthService,
        @Autowired private val facebookOAuthService: FacebookOAuthService,
        @Autowired private val googleOAuthService: GoogleOAuthService,
        @Autowired private val naverOAuthService: NaverOAuthService,
        @Autowired private val githubOAuthService: GithubOAuthService,
        @Autowired private val discordOAuthService: DiscordOAuthService,
        @Autowired private val userDao: UserDao
) {
    private val logger = LoggerFactory.getLogger(LoginService::class.java)

    fun getAuthorizationUrl(session: HttpSession, vendorType: VendorType): String? {
        return getService(vendorType).getAuthorizationUrl(session)
    }

    fun login(request: HttpServletRequest, vendorType: VendorType, code: String, state: String): Boolean {
        val loginResult = getService(vendorType).login(request, code, state)

        logger.info("${request.session.id} 세션에서 ${vendorType.name} 로그인에 ${if (loginResult) "성공" else "실패"}했습니다.")
        return loginResult
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
                image = oAuthUser.profileImage
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
        }
    }
}