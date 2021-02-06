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

package me.horyu.kkutuweb.admin.api

import me.horyu.kkutuweb.admin.api.response.ConnectionLogResponse
import me.horyu.kkutuweb.admin.service.ConnectionLogService
import me.horyu.kkutuweb.login.LoginService
import me.horyu.kkutuweb.setting.KKuTuSetting
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/api/admin/connection-logs")
class ConnectionLogApi(
    @Autowired private val setting: KKuTuSetting,
    @Autowired private val loginService: LoginService,
    @Autowired private val connectionLogService: ConnectionLogService
) {
    private val logger = LoggerFactory.getLogger(ConnectionLogApi::class.java)

    @GetMapping
    fun getConnectionLog(
        @RequestParam(required = true, name = "page") page: Int,
        @RequestParam(required = true, name = "size") pageSize: Int,
        @RequestParam(required = true, name = "sort") sortData: String,
        @RequestParam(required = false, name = "user_id", defaultValue = "") userId: String,
        @RequestParam(required = false, name = "user_name", defaultValue = "") userName: String,
        @RequestParam(required = false, name = "user_ip", defaultValue = "") userIp: String,
        @RequestParam(required = false, name = "channel", defaultValue = "") channel: String,
        @RequestParam(required = false, name = "user_agent", defaultValue = "") userAgent: String,
        @RequestParam(required = false, name = "finger_print_2", defaultValue = "") fingerPrint2: String,
        session: HttpSession
    ): ConnectionLogResponse {
        val sessionProfile = loginService.getSessionProfile(session)
        if (sessionProfile == null) {
            logger.warn("인증되지 않은 사용자로 부터 접속 로그 조회 요청이 차단되었습니다.")
            return ConnectionLogResponse(0, emptyList())
        }

        if (!setting.getAdminIds().contains(sessionProfile.id)) {
            logger.warn("인증되지 않은 사용자(${sessionProfile.id})로 부터 접속 로그 조회 요청이 차단되었습니다.")
            return ConnectionLogResponse(0, emptyList())
        }

        val searchFilters = mapOf(
            "user_id" to userId,
            "user_name" to userName,
            "user_ip" to userIp,
            "channel" to channel,
            "user_agent" to userAgent,
            "finger_print_2" to fingerPrint2
        )

        return connectionLogService.getConnectionLogRes(page, pageSize, sortData, searchFilters)
    }
}