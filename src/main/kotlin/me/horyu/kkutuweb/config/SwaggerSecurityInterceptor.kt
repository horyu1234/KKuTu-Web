/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2021. horyu1234(admin@horyu.me)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.horyu.kkutuweb.config

import me.horyu.kkutuweb.login.LoginService
import me.horyu.kkutuweb.setting.KKuTuSetting
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SwaggerSecurityInterceptor(
    @Autowired private val loginService: LoginService,
    @Autowired private val setting: KKuTuSetting
) : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(SwaggerSecurityInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val session = request.session
        val sessionProfile = loginService.getSessionProfile(session)

        if (sessionProfile == null) {
            logger.warn("인증되지 않은 사용자로 부터 Swagger2 페이지 접근 요청이 차단되었습니다.")
            response.sendRedirect("/")
            return false
        }

        if (!setting.getAdminIds().contains(sessionProfile.id)) {
            logger.warn("인증되지 않은 사용자(${sessionProfile.id})로 부터 Swagger2 페이지 접근 요청이 차단되었습니다.")
            response.sendRedirect("/")
            return false
        }

        return true
    }
}