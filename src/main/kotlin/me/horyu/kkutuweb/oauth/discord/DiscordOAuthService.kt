package me.horyu.kkutuweb.oauth.discord

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.scribejava.apis.DiscordApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth20Service
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
class DiscordOAuthService(
        @Value("\${oauth.discord.client-id}") private val discordApiKey: String,
        @Value("\${oauth.discord.client-secret}") private val discordApiSecret: String,
        @Value("\${oauth.discord.callback-url}") private val discordApiCallbackUrl: String,
        @Autowired private val objectMapper: ObjectMapper
) : OAuthService() {
    private val logger = LoggerFactory.getLogger(DiscordOAuthService::class.java)
    private val protectedResourceUrl = "https://discordapp.com/api/users/@me"

    @PostConstruct
    private fun init() {
        oAuth20Service = ServiceBuilder(discordApiKey)
                .apiSecret(discordApiSecret)
                .callback(discordApiCallbackUrl)
                .defaultScope("identify")
                .userAgent("KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)")
                .build(DiscordApi.instance())
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
            val jsonResponse = objectMapper.readTree(response.body)

            val oAuthUser = OAuthUser(VendorType.DISCORD,
                    jsonResponse["id"].longValue().toString(),
                    jsonResponse["username"].textValue() + "#" + jsonResponse["discriminator"].textValue(),
                    "https://cdn.discordapp.com/avatars/${jsonResponse["id"].longValue()}/${jsonResponse["avatar"].textValue()}",
                    Gender.OTHER,
                    0,
                    0)

            httpSession.setAttribute(SessionAttribute.IS_GUEST.attributeName, false)
            httpSession.setAttribute(SessionAttribute.OAUTH_USER.attributeName, oAuthUser)
            return true
        } catch (exception: Exception) {
            exception.printStackTrace()
            logger.warn("${httpSession.id} 세션에서 로그인에 실패하였습니다. ${exception.message}")
            return false
        }
    }
}