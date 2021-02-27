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

package me.horyu.kkutuweb.admin.service

import me.horyu.kkutuweb.admin.SortType
import me.horyu.kkutuweb.admin.api.request.WordEditRequest
import me.horyu.kkutuweb.admin.api.response.ListResponse
import me.horyu.kkutuweb.admin.dao.WordAuditLogDAO
import me.horyu.kkutuweb.admin.domain.WordAuditLog
import me.horyu.kkutuweb.admin.vo.WordVO
import me.horyu.kkutuweb.word.Word
import me.horyu.kkutuweb.word.WordDao
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AdminWordService(
    @Autowired private val wordDao: WordDao,
    @Autowired private val wordAuditLogDAO: WordAuditLogDAO
) {
    private val logger = LoggerFactory.getLogger(AdminWordService::class.java)

    fun getWordListRes(
        lang: String,
        page: Int,
        pageSize: Int,
        sortData: String,
        searchFilters: Map<String, String>
    ): ListResponse<WordVO> {
        val tableName = getTableName(lang)
        if (tableName.isEmpty()) {
            return ListResponse(0, emptyList())
        }

        val split = sortData.split(",")
        val sortField = when (split[0]) {
            "word" -> "_id"
            "hit" -> "hit"
            "flag" -> "flag"
            else -> ""
        }
        val sortType = SortType.valueOf(split[1])

        val dbSearchFilters = searchFilters.filterValues { it.isNotEmpty() }

        val dataCount = wordDao.getDataCount(tableName, dbSearchFilters)
        val pageData = wordDao.getPageData(tableName, page, pageSize, sortField, sortType, dbSearchFilters).map {
            WordVO.convertFrom(it)
        }

        return ListResponse(dataCount, pageData)
    }

    fun getWords(lang: String, wordName: String): ListResponse<WordVO> {
        val tableName = getTableName(lang)
        if (tableName.isEmpty()) {
            return ListResponse(0, emptyList())
        }

        val words = wordDao.getWords(tableName, wordName).map {
            WordVO.convertFrom(it)
        }
        return ListResponse(words.size, words)
    }

    fun editWord(adminId: String, lang: String, wordName: String, wordEditRequest: WordEditRequest) {
        val tableName = getTableName(lang)
        val words = wordDao.getWords(tableName, wordName)
        if (words.size != 1) {
            logger.warn("수정하려는 단어 데이터가 1개가 아닙니다. 언어: $lang 단어: $wordName")
            return
        }

        val oldWord = words[0]
        val newWord = Word.convertFrom(
            WordVO(
                word = wordName,
                hit = 0,
                flags = wordEditRequest.flags,
                details = wordEditRequest.details
            )
        )

        wordDao.update(
            tableName, wordName, mapOf(
                "type" to newWord.type,
                "mean" to newWord.mean,
                "flag" to newWord.flag,
                "theme" to newWord.theme
            )
        )
        wordAuditLogDAO.insert(
            lang, WordAuditLog(
                time = LocalDateTime.now(),
                word = wordName,
                type = WordAuditLog.WordAuditLogType.UPDATE,
                oldType = oldWord.type,
                oldMean = oldWord.mean,
                oldFlag = oldWord.flag,
                oldTheme = oldWord.theme,
                newType = newWord.type,
                newMean = newWord.mean,
                newFlag = newWord.flag,
                newTheme = newWord.theme,
                admin = adminId
            )
        )
    }

    fun deleteWord(adminId: String, lang: String, wordName: String) {
        val tableName = getTableName(lang)
        val words = wordDao.getWords(tableName, wordName)
        if (words.size != 1) {
            logger.warn("삭제하려는 단어 데이터가 1개가 아닙니다. 언어: $lang 단어: $wordName")
            return
        }

        val oldWord = words[0]
        wordDao.remove(tableName, wordName)
        wordAuditLogDAO.insert(
            lang, WordAuditLog(
                time = LocalDateTime.now(),
                word = wordName,
                type = WordAuditLog.WordAuditLogType.DELETE,
                oldType = oldWord.type,
                oldMean = oldWord.mean,
                oldFlag = oldWord.flag,
                oldTheme = oldWord.theme,
                admin = adminId
            )
        )
    }

    private fun getTableName(lang: String): String {
        return when (lang) {
            "ko" -> "kkutu_ko"
            "en" -> "kkutu_en"
            else -> ""
        }
    }
}