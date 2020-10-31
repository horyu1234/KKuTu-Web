package me.horyu.kkutuweb.oauth.daldalso

import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme

object DaldalsoApi : DefaultApi20() {
    override fun getAccessTokenEndpoint(): String {
        return "https://daldal.so/oauth/token"
    }

    override fun getAuthorizationBaseUrl(): String {
        return "https://daldal.so/oauth/authorize"
    }

    override fun getClientAuthentication(): ClientAuthentication {
        return RequestBodyAuthenticationScheme.instance()
    }
}