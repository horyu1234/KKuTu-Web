package me.horyu.kkutuweb.extension

import me.horyu.kkutuweb.SessionAttribute
import me.horyu.kkutuweb.oauth.OAuthUser
import javax.servlet.http.HttpSession

fun HttpSession.isGuest(): Boolean = (this.getAttribute(SessionAttribute.IS_GUEST.attributeName) ?: true) as Boolean
fun HttpSession.getOAuthUser(): OAuthUser = this.getAttribute(SessionAttribute.OAUTH_USER.attributeName) as OAuthUser