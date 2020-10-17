package me.horyu.kkutuweb.oauth.google

import com.github.scribejava.apis.GoogleApi20
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
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpSession

@Service
class GoogleOAuthService(
        @Value("\${oauth.google.client-id}") private val googleApiKey: String,
        @Value("\${oauth.google.client-secret}") private val googleApiSecret: String,
        @Value("\${oauth.google.callback-url}") private val googleApiCallbackUrl: String,
        @Autowired private val gson: Gson
) : OAuthService() {
    private val logger = LoggerFactory.getLogger(GoogleOAuthService::class.java)
    private val protectedResourceUrl = "https://www.googleapis.com/plus/v1/people/me"

    @PostConstruct
    private fun init() {
        oAuth20Service = ServiceBuilder(googleApiKey)
                .apiSecret(googleApiSecret)
                .defaultScope("profile https://www.googleapis.com/auth/plus.login")
                .callback(googleApiCallbackUrl)
                .build(GoogleApi20.instance())
    }

    override fun getAuthorizationUrl(httpSession: HttpSession): String {
        val randomState = getRandomState()
        httpSession.setAttribute(SessionAttribute.OAUTH_STATE.attributeName, randomState)
        httpSession.setAttribute(SessionAttribute.OAUTH_20_SERVICE.attributeName, oAuth20Service)

        val additionalParams = HashMap<String, String>()
        additionalParams["access_type"] = "offline"

        return oAuth20Service.createAuthorizationUrlBuilder()
                .state(randomState)
                .additionalParams(additionalParams)
                .build()
    }

    override fun abstractLogin(httpSession: HttpSession, oAuth20Service: OAuth20Service, code: String): Boolean {
        try {
            val accessToken = oAuth20Service.getAccessToken(code)

            val request = OAuthRequest(Verb.GET, protectedResourceUrl)
            oAuth20Service.signRequest(accessToken, request)

            val response = oAuth20Service.execute(request)

            val googleResponse = gson.fromJson(response.body, GoogleResponse::class.java)
            val oAuthUser = OAuthUser(VendorType.GOOGLE,
                    googleResponse.id,
                    googleResponse.displayName,
                    googleResponse.image.url,
                    Gender.fromName(googleResponse.gender ?: ""),
                    if (googleResponse.ageRange == null) -1 else googleResponse.ageRange.min,
                    if (googleResponse.ageRange == null) -1 else googleResponse.ageRange.max)

            httpSession.setAttribute(SessionAttribute.IS_GUEST.attributeName, false)
            httpSession.setAttribute(SessionAttribute.OAUTH_USER.attributeName, oAuthUser)
            return true
        } catch (exception: Exception) {
            logger.warn("${httpSession.id} 세션에서 로그인에 실패하였습니다. ${exception.message}")
            return false
        }
    }
}