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

import me.horyu.kkutuweb.admin.api.request.WordEditRequest
import me.horyu.kkutuweb.admin.api.response.ListResponse
import me.horyu.kkutuweb.admin.service.AdminWordService
import me.horyu.kkutuweb.admin.vo.WordVO
import me.horyu.kkutuweb.extension.getIp
import me.horyu.kkutuweb.login.LoginService
import me.horyu.kkutuweb.setting.KKuTuSetting
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
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
    fun getWordList(
        @PathVariable lang: String,
        @RequestParam(required = true, name = "page") page: Int,
        @RequestParam(required = true, name = "size") pageSize: Int,
        @RequestParam(required = true, name = "sort") sortData: String,
        @RequestParam(required = false, defaultValue = "") word: String,
        request: HttpServletRequest, session: HttpSession
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

        val wordListRes = adminWordService.getWordListRes(lang, page, pageSize, sortData, searchFilters)
        logger.info("[${request.getIp()}] ${sessionProfile.id} 님이 단어 목록을 요청했습니다. 언어: $lang / 검색어: $word / 총 개수: ${wordListRes.totalElements}")

        return wordListRes
    }

    @GetMapping("/{lang}/{word}")
    fun getWord(
        @PathVariable lang: String,
        @PathVariable word: String,
        request: HttpServletRequest, session: HttpSession
    ): ListResponse<WordVO> {
        val sessionProfile = loginService.getSessionProfile(session)
        if (sessionProfile == null) {
            logger.warn("인증되지 않은 사용자로 부터 단어 조회 요청이 차단되었습니다.")
            return ListResponse(0, emptyList())
        }

        if (!setting.getAdminIds().contains(sessionProfile.id)) {
            logger.warn("인증되지 않은 사용자(${sessionProfile.id})로 부터 단어 조회 요청이 차단되었습니다.")
            return ListResponse(0, emptyList())
        }

        logger.info("[${request.getIp()}] ${sessionProfile.id} 님이 단어 정보를 요청했습니다. 언어: $lang / 단어: $word")
        return adminWordService.getWords(lang, word)
    }

    @PatchMapping("/{lang}/{word}")
    fun editWord(
        @PathVariable lang: String,
        @PathVariable word: String,
        @RequestBody wordEditRequest: WordEditRequest,
        request: HttpServletRequest, session: HttpSession
    ) {
        val sessionProfile = loginService.getSessionProfile(session)
        if (sessionProfile == null) {
            logger.warn("인증되지 않은 사용자로 부터 단어 수정 요청이 차단되었습니다.")
            return
        }

        if (!setting.getAdminIds().contains(sessionProfile.id)) {
            logger.warn("인증되지 않은 사용자(${sessionProfile.id})로 부터 단어 수정 요청이 차단되었습니다.")
            return
        }

        adminWordService.editWord(sessionProfile.id, lang, word, wordEditRequest)
        logger.info("[${request.getIp()}] ${sessionProfile.id} 님이 단어를 수정했습니다. 언어: $lang / 단어: $word")
    }

    @DeleteMapping("/{lang}/{word}")
    fun deleteWord(
        @PathVariable lang: String,
        @PathVariable word: String,
        request: HttpServletRequest, session: HttpSession
    ) {
        val sessionProfile = loginService.getSessionProfile(session)
        if (sessionProfile == null) {
            logger.warn("인증되지 않은 사용자로 부터 단어 삭제 요청이 차단되었습니다.")
            return
        }

        if (!setting.getAdminIds().contains(sessionProfile.id)) {
            logger.warn("인증되지 않은 사용자(${sessionProfile.id})로 부터 단어 삭제 요청이 차단되었습니다.")
            return
        }

        adminWordService.deleteWord(sessionProfile.id, lang, word)
        logger.info("[${request.getIp()}] ${sessionProfile.id} 님이 단어를 삭제했습니다. 언어: $lang / 단어: $word")
    }
}