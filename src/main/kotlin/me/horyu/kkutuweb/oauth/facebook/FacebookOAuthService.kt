package me.horyu.kkutuweb.oauth.facebook

import com.github.scribejava.apis.FacebookApi
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
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.servlet.http.HttpSession

@Service
class FacebookOAuthService(
        @Value("\${oauth.facebook.client-id}") private val facebookApiKey: String,
        @Value("\${oauth.facebook.client-secret}") private val facebookApiSecret: String,
        @Value("\${oauth.facebook.callback-url}") private val facebookApiCallbackUrl: String,
        @Autowired private val gson: Gson
) : OAuthService() {
    private val logger = LoggerFactory.getLogger(FacebookOAuthService::class.java)
    private val protectedResourceUrl = "https://graph.facebook.com/v2.11/me?fields=id,name,gender,age_range"

    @PostConstruct
    private fun init() {
        oAuth20Service = ServiceBuilder(facebookApiKey)
                .apiSecret(facebookApiSecret)
                .callback(facebookApiCallbackUrl)
                .build(FacebookApi.instance())
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

            val facebookResponse = gson.fromJson(response.body, FacebookResponse::class.java)
            val oAuthUser = OAuthUser(VendorType.FACEBOOK,
                    facebookResponse.id,
                    facebookResponse.name,
                    facebookResponse.getProfileImage(),
                    Gender.fromName(facebookResponse.gender),
                    facebookResponse.ageRange.min,
                    facebookResponse.ageRange.max)

            httpSession.setAttribute(SessionAttribute.IS_GUEST.attributeName, false)
            httpSession.setAttribute(SessionAttribute.OAUTH_USER.attributeName, oAuthUser)
            return true
        } catch (exception: Exception) {
            logger.warn("${httpSession.id} 세션에서 로그인에 실패하였습니다. ${exception.message}")
            return false
        }
    }
}