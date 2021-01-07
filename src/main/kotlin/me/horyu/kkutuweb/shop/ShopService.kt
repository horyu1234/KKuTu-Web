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

package me.horyu.kkutuweb.shop

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.ObjectNode
import me.horyu.kkutuweb.extension.getOAuthUser
import me.horyu.kkutuweb.extension.isGuest
import me.horyu.kkutuweb.extension.toJson
import me.horyu.kkutuweb.shop.response.ResponseGood
import me.horyu.kkutuweb.shop.response.ResponseGoodDetail
import me.horyu.kkutuweb.shop.response.ShopResponse
import me.horyu.kkutuweb.user.UserDao
import org.postgresql.util.PGobject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpSession
import kotlin.math.roundToInt

@Service
class ShopService(
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val shopDao: ShopDao,
    @Autowired private val shopDetailDao: ShopDetailDao,
    @Autowired private val userDao: UserDao
) {
    private val logger = LoggerFactory.getLogger(ShopService::class.java)

    fun getGoods(): ShopResponse {
        return ShopResponse(shopDao.getGoods().map {
            ResponseGood.fromGood(it)
        })
    }

    fun buyGood(id: String, session: HttpSession): String {
        if (session.isGuest()) return "{\"error\":423}"
        val oAuthUser = session.getOAuthUser()

        val good = shopDao.getGood(id) ?: return "{\"error\":400}"
        if (good.cost < 0) return "{\"error\":400}"

        val userId = oAuthUser.getUserId()
        val user = userDao.getUser(userId) ?: return "{\"error\":400}"

        val afterBuyMoney = user.money - good.cost
        if (afterBuyMoney < 0) return "{\"error\":400}"

        obtainGood(user.box, id, 1, good.term)

        val userBoxJsonObj = PGobject()
        userBoxJsonObj.type = "json"
        userBoxJsonObj.value = user.box.toJson()

        userDao.updateUser(
            user.id, mapOf(
                "money" to afterBuyMoney,
                "box" to userBoxJsonObj
            )
        )
        shopDao.increaseHit(id)

        val userName = if (user.nickname == null) userId else "${user.nickname} ($userId)"
        logger.info("$userName 님이 $id 상품을 구매했습니다.")

        return "{\"result\": 200, \"money\": $afterBuyMoney, \"box\": ${user.box.toJson()}}"
    }

    fun paybackGood(id: String, session: HttpSession): String {
        if (session.isGuest()) return "{\"error\":400}"
        val oAuthUser = session.getOAuthUser()

        val userId = oAuthUser.getUserId()
        val user = userDao.getUser(userId) ?: return "{\"error\":400}"

        val box = user.box
        if (!box.has(id)) return "{\"error\":430}"

        val isDyn = id.startsWith("$")
        val good = shopDao.getGood(if (isDyn) id.substring(0, 4) else id) ?: return "{\"error\":430}"

        consumeGood(user.box, id, 1, true)

        val userBoxJsonObj = PGobject()
        userBoxJsonObj.type = "json"
        userBoxJsonObj.value = user.box.toJson()

        val afterPaybackMoney = user.money + (0.2 * good.cost).roundToInt()
        userDao.updateUser(
            user.id, mapOf(
                "money" to afterPaybackMoney,
                "box" to userBoxJsonObj
            )
        )

        val userName = if (user.nickname == null) userId else "${user.nickname} ($userId)"
        logger.info("$userName 님이 $id 상품을 환불했습니다.")

        return "{\"result\": 200, \"money\": $afterPaybackMoney, \"box\": ${user.box.toJson()}}"
    }

    fun obtainGood(box: JsonNode, goodId: String, value: Int, term: Int?, addValue: Boolean = false) {
        val boxObjectNode = box as ObjectNode

        if (term == null || term == 0) {
            boxObjectNode.put(
                goodId,
                (if (boxObjectNode.has(goodId)) boxObjectNode.get(goodId).intValue() else 0) + value
            )
        } else {
            if (boxObjectNode.has(goodId)) {
                val goodJson = boxObjectNode.get(goodId) as ObjectNode

                if (addValue) goodJson.put("value", goodJson["value"].intValue() + value)
                else goodJson.put("expire", goodJson["expire"].intValue() + term)
            } else {
                val currentTime = System.currentTimeMillis()

                val newGoodJson = objectMapper.createObjectNode()
                newGoodJson.put("value", value)
                newGoodJson.put("expire", (currentTime * 0.001 + term).roundToInt())

                boxObjectNode.set(goodId, newGoodJson)
            }
        }
    }

    fun consumeGood(box: JsonNode, goodId: String, value: Int, force: Boolean = false) {
        val boxObjectNode = box as ObjectNode
        val goodJson = boxObjectNode.get(goodId)

        if (goodJson is ObjectNode) {
            // 기한이 끝날 때까지 box 자체에서 사라지지는 않는다. 기한 만료 여부 확인 시점: 1. 로그인 2. box 조회 3. 게임 결과 반영 직전 4. 해당 항목 사용 직전
            val afterConsumeValue = goodJson["value"].intValue() - value
            goodJson.put("value", afterConsumeValue)

            if (afterConsumeValue <= 0) {
                if (force || !goodJson.has("expire") && goodJson["expire"].intValue() == 0) boxObjectNode.remove(
                    goodId
                )
            }
        } else if (goodJson is IntNode) {
            val afterConsumeCount = goodJson.intValue() - value
            boxObjectNode.put(goodId, afterConsumeCount)

            if (afterConsumeCount <= 0) {
                boxObjectNode.remove(goodId)
            }
        }
    }

    fun getGoodDetails(): Map<String, ResponseGoodDetail> {
        val resultMap = HashMap<String, ResponseGoodDetail>()

        val goodDetails = shopDetailDao.getGoodDetails()
        for (goodDetail in goodDetails) {
            resultMap[goodDetail.id] = ResponseGoodDetail(goodDetail.namekoKR, goodDetail.desckoKR)
        }

        return resultMap
    }
}