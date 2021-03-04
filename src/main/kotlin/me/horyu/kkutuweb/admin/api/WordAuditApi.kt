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

import me.horyu.kkutuweb.admin.api.response.ListResponse
import me.horyu.kkutuweb.admin.domain.WordAuditLog
import me.horyu.kkutuweb.admin.service.AdminWordAuditService
import me.horyu.kkutuweb.extension.getIp
import me.horyu.kkutuweb.login.LoginService
import me.horyu.kkutuweb.setting.AdminSetting
import me.horyu.kkutuweb.setting.KKuTuSetting
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/api/admin/word-audits")
class WordAuditApi(
    @Autowired private val setting: KKuTuSetting,
    @Autowired private val loginService: LoginService,
    @Autowired private val adminWordAuditService: AdminWordAuditService
) {
    private val logger = LoggerFactory.getLogger(WordAuditApi::class.java)

    @GetMapping("/{lang}")
    fun getWordAuditList(
        @PathVariable lang: String,
        @RequestParam(required = true, name = "  ") page: Int,
        @RequestParam(required = true, name = "size") pageSize: Int,
        @RequestParam(required = true, name = "sort") sortData: String,
        request: HttpServletRequest, session: HttpSession
    ): ListResponse<WordAuditLog> {
        val sessionProfile = loginService.getSessionProfile(session)
        if (sessionProfile == null) {
            logger.warn("인증되지 않은 사용자로 부터 단어 관리 로그 조회 요청이 차단되었습니다.")
            return ListResponse(0, emptyList())
        }

        if (!setting.getAdminIds().contains(sessionProfile.id)) {
            logger.warn("관리자가 아닌 사용자(${sessionProfile.id})로 부터 단어 관리 로그 조회 요청이 차단되었습니다.")
            return ListResponse(0, emptyList())
        }

        val adminSetting = setting.getAdmins().find { it.id == sessionProfile.id }!!
        if (!adminSetting.privileges.contains(AdminSetting.Privilege.WORD)) {
            logger.warn("기능 권한이 없는 관리자(${sessionProfile.id})로 부터 단어 관리 로그 조회 요청이 차단되었습니다.")
            return ListResponse(0, emptyList())
        }

        val wordListRes = adminWordAuditService.getWordAuditListRes(lang, page, pageSize, sortData)
        logger.info("[${request.getIp()}] ${sessionProfile.id} 님이 단어 관리 로그를 요청했습니다. 언어: $lang / 총 개수: ${wordListRes.totalElements}")

        return wordListRes
    }
}