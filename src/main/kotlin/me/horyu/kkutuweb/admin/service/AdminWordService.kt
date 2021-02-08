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
import me.horyu.kkutuweb.admin.api.response.ListResponse
import me.horyu.kkutuweb.admin.vo.WordVO
import me.horyu.kkutuweb.word.WordDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AdminWordService(
    @Autowired private val wordDao: WordDao
) {
    fun getWordListRes(
        lang: String,
        page: Int,
        pageSize: Int,
        sortData: String,
        searchFilters: Map<String, String>
    ): ListResponse<WordVO> {
        val tableName = when (lang) {
            "ko" -> "kkutu_ko"
            "en" -> "kkutu_en"
            else -> ""
        }
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
}