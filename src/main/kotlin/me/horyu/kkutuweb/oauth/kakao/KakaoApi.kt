/*
 * scribejava (https://github.com/scribejava/scribejava)
 *
 * The MIT License
 *
 * Copyright (c) 2010 Pablo Fernandez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.horyu.kkutuweb.oauth.kakao

import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme

object KakaoApi : DefaultApi20() {
    override fun getAccessTokenEndpoint(): String {
        return "https://kauth.kakao.com/oauth/token"
    }

    override fun getAuthorizationBaseUrl(): String {
        return "https://kauth.kakao.com/oauth/authorize"
    }

    override fun getClientAuthentication(): ClientAuthentication {
        return RequestBodyAuthenticationScheme.instance()
    }
}