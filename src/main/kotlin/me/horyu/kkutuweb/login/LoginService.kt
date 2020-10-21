package me.horyu.kkutuweb.login

import me.horyu.kkutuweb.extension.getOAuthUser
import me.horyu.kkutuweb.extension.isGuest
import me.horyu.kkutuweb.oauth.VendorType
import me.horyu.kkutuweb.oauth.facebook.FacebookOAuthService
import me.horyu.kkutuweb.oauth.github.GithubOAuthService
import me.horyu.kkutuweb.oauth.google.GoogleOAuthService
import me.horyu.kkutuweb.oauth.naver.NaverOAuthService
import me.horyu.kkutuweb.session.SessionProfile
import me.horyu.kkutuweb.user.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Service
class LoginService(
        @Autowired private val facebookOAuthService: FacebookOAuthService,
        @Autowired private val googleOAuthService: GoogleOAuthService,
        @Autowired private val naverOAuthService: NaverOAuthService,
        @Autowired private val githubOAuthService: GithubOAuthService,
        @Autowired private val userDao: UserDao
) {
    fun getAuthorizationUrl(session: HttpSession, vendorType: VendorType): String? {
        return when (vendorType) {
            VendorType.FACEBOOK -> facebookOAuthService.getAuthorizationUrl(session)
            VendorType.GOOGLE -> googleOAuthService.getAuthorizationUrl(session)
            VendorType.NAVER -> naverOAuthService.getAuthorizationUrl(session)
            VendorType.GITHUB -> githubOAuthService.getAuthorizationUrl(session)
        }
    }

    fun login(request: HttpServletRequest, vendorType: VendorType, code: String, state: String): Boolean {
        return when (vendorType) {
            VendorType.FACEBOOK -> facebookOAuthService.login(request, code, state)
            VendorType.GOOGLE -> googleOAuthService.login(request, code, state)
            VendorType.NAVER -> naverOAuthService.login(request, code, state)
            VendorType.GITHUB -> githubOAuthService.login(request, code, state)
        }
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
}