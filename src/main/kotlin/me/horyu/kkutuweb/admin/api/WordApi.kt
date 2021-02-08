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
import me.horyu.kkutuweb.admin.service.AdminWordService
import me.horyu.kkutuweb.admin.vo.WordVO
import me.horyu.kkutuweb.login.LoginService
import me.horyu.kkutuweb.setting.KKuTuSetting
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/api/admin/words")
class WordApi(
    @Autowired private val setting: KKuTuSetting,
    @Autowired private val loginService: LoginService,
    @Autowired private val adminWordService: AdminWordService
) {
    private val logger = LoggerFactory.getLogger(WordApi::class.java)

    @GetMapping("/{lang}")
    fun getConnectionLog(
        @PathVariable lang: String,
        @RequestParam(required = true, name = "page") page: Int,
        @RequestParam(required = true, name = "size") pageSize: Int,
        @RequestParam(required = true, name = "sort") sortData: String,
        @RequestParam(required = false, defaultValue = "") word: String,
        session: HttpSession
    ): ListResponse<WordVO> {
        val sessionProfile = loginService.getSessionProfile(session)
        if (sessionProfile == null) {
            logger.warn("인증되지 않은 사용자로 부터 단어 목록 조회 요청이 차단되었습니다.")
            return ListResponse(0, emptyList())
        }

        if (!setting.getAdminIds().contains(sessionProfile.id)) {
            logger.warn("인증되지 않은 사용자(${sessionProfile.id})로 부터 단어 목록 조회 요청이 차단되었습니다.")
            return ListResponse(0, emptyList())
        }

        val searchFilters = mapOf(
            "_id" to word
        )

        return adminWordService.getWordListRes(lang, page, pageSize, sortData, searchFilters)
    }
}