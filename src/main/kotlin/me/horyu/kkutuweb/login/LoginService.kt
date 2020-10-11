package me.horyu.kkutuweb.login

import me.horyu.kkutuweb.oauth.VendorType
import me.horyu.kkutuweb.oauth.facebook.FacebookOAuthService
import me.horyu.kkutuweb.oauth.google.GoogleOAuthService
import me.horyu.kkutuweb.oauth.naver.NaverOAuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Service
class LoginService(
        @Autowired private val facebookOAuthService: FacebookOAuthService,
        @Autowired private val googleOAuthService: GoogleOAuthService,
        @Autowired private val naverOAuthService: NaverOAuthService
) {
    fun getAuthorizationUrl(session: HttpSession, vendorType: VendorType): String? {
        return when (vendorType) {
            VendorType.FACEBOOK -> facebookOAuthService.getAuthorizationUrl(session)
            VendorType.GOOGLE -> googleOAuthService.getAuthorizationUrl(session)
            VendorType.NAVER -> naverOAuthService.getAuthorizationUrl(session)
        }
    }

    fun login(request: HttpServletRequest, vendorType: VendorType, code: String, state: String): Boolean {
        return when (vendorType) {
            VendorType.FACEBOOK -> facebookOAuthService.login(request, code, state)
            VendorType.GOOGLE -> googleOAuthService.login(request, code, state)
            VendorType.NAVER -> naverOAuthService.login(request, code, state)
        }
    }
}