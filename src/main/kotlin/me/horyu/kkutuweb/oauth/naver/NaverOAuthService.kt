package me.horyu.kkutuweb.oauth.naver

import com.github.scribejava.apis.NaverApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth20Service
import com.google.gson.Gson
import me.horyu.kkutuweb.SessionAttribute
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
        @Autowired private val gson: Gson
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

            val naverResponse = gson.fromJson(response.body, NaverResponse::class.java)

            val splitAge = naverResponse.realResponse.age.split("-")
            val oAuthUser = OAuthUser(VendorType.NAVER,
                    naverResponse.realResponse.id,
                    naverResponse.realResponse.nickname,
                    naverResponse.realResponse.profileImage,
                    Gender.fromName(naverResponse.realResponse.gender),
                    splitAge[0].toInt(),
                    splitAge[1].toInt())

            httpSession.setAttribute(SessionAttribute.IS_GUEST.attributeName, false)
            httpSession.setAttribute(SessionAttribute.OAUTH_USER.attributeName, oAuthUser)
            return true
        } catch (exception: Exception) {
            logger.warn("${httpSession.id} 세션에서 로그인에 실패하였습니다. ${exception.message}")
            return false
        }
    }
}