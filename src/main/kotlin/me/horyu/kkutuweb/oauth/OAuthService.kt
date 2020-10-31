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