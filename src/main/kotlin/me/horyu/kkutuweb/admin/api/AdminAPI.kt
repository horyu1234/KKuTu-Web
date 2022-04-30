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

import me.horyu.kkutuweb.extension.getIp
import me.horyu.kkutuweb.game.GameClientManager
import me.horyu.kkutuweb.ranking.RankDao
import me.horyu.kkutuweb.setting.KKuTuSetting
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/admin")
class AdminAPI(
    @Autowired private val kKuTuSetting: KKuTuSetting,
    @Autowired private val gameClientManager: GameClientManager,
    @Autowired private val rankDao: RankDao
) {
    private val logger = LoggerFactory.getLogger(AdminAPI::class.java)

    @GetMapping("/kickByUserId/{id}")
    fun kickByUserId(
        @PathVariable id: String,
        @RequestParam apiKey: String,
        request: HttpServletRequest
    ) {
        if (kKuTuSetting.getApiKey() != apiKey) {
            logger.warn("[${request.getIp()}] API 키가 불일치하여 kickByUserId/$id 요청을 무시합니다.")
            return
        }

        gameClientManager.kick(id, "")
        logger.info("$id 계정을 끄투리오에서 추방합니다.")
    }

    @GetMapping("/kickByIp/{ip}")
    fun kickByIp(
        @PathVariable ip: String,
        @RequestParam apiKey: String,
        request: HttpServletRequest
    ) {
        if (kKuTuSetting.getApiKey() != apiKey) {
            logger.warn("[${request.getIp()}] API 키가 불일치하여 kickByIp/$ip 요청을 무시합니다.")
            return
        }

        gameClientManager.kick("", ip)
        logger.info("$ip 아이피로 접속 중인 계정을 끄투리오에서 추방합니다.")
    }

    @GetMapping("/resetRank/{id}")
    fun resetRank(
        @PathVariable id: String,
        @RequestParam apiKey: String,
        request: HttpServletRequest
    ) {
        if (kKuTuSetting.getApiKey() != apiKey) {
            logger.warn("[${request.getIp()}] API 키가 불일치하여 resetRank/$id 요청을 무시합니다.")
            return
        }

        rankDao.remove(id)
        logger.info("$id 계정의 랭킹 데이터를 제거했습니다.")
    }

    @PostMapping("/yell")
    fun yell(
        @RequestParam value: String,
        @RequestParam apiKey: String
    ) {
        if (kKuTuSetting.getApiKey() != apiKey) {
            logger.warn("[${request.getIp()}] API 키가 불일치하여 yell 요청을 무시합니다.")
            return
        }

        gameClientManager.yell(value)
        logger.info("공지가 전송되었습니다. 내용 : $value")
    }
}