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

package me.horyu.kkutuweb.ranking

import me.horyu.kkutuweb.ranking.response.RankResponse
import me.horyu.kkutuweb.ranking.response.ResponseRank
import me.horyu.kkutuweb.user.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class RankingService(
    @Autowired private val rankDao: RankDao,
    @Autowired private val userDao: UserDao
) {
    fun getRanking(p: Long?, id: String?): RankResponse {
        return if (id == null) {
            val page = p ?: 0L
            val ranks = rankDao.getPage(page, 15)
            RankResponse(page, ranks.map { ResponseRank.fromRank(it, getNickname(it.id)) })
        } else {
            val ranks = rankDao.getSurround(id, 15)
            RankResponse(0, ranks.map { ResponseRank.fromRank(it, getNickname(it.id)) })
        }
    }

    @Cacheable(value = ["RankingService.nicknameCache"], key = "#id")
    fun getNickname(id: String): String? {
        val user = userDao.getUser(id) ?: return null
        return user.nickname
    }

    @CacheEvict(value = ["RankingService.nicknameCache"], key = "#id")
    fun clearNicknameCache(id: String) {
        println("clear - id = [${id}]")
    }
}