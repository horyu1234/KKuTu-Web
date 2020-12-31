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

package me.horyu.kkutuweb.consume

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import me.horyu.kkutuweb.extension.getOAuthUser
import me.horyu.kkutuweb.extension.isGuest
import me.horyu.kkutuweb.extension.toJson
import me.horyu.kkutuweb.shop.ShopDao
import me.horyu.kkutuweb.shop.ShopService
import me.horyu.kkutuweb.user.User
import me.horyu.kkutuweb.user.UserDao
import org.postgresql.util.PGobject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpSession
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Service
class ConsumeService(
        @Autowired private val objectMapper: ObjectMapper,
        @Autowired private val userDao: UserDao,
        @Autowired private val shopDao: ShopDao,
        @Autowired private val shopService: ShopService
) {
    fun consume(id: String, session: HttpSession): String {
        if (session.isGuest()) return "{\"error\":400}"
        val oAuthUser = session.getOAuthUser()

        val userId = "${oAuthUser.vendorType.name.toLowerCase()}-${oAuthUser.vendorId}"
        val user = userDao.getUser(userId) ?: return "{\"error\":400}"

        if (!user.box.has(id)) return "{\"error\":430}"

        val isDyn = id.startsWith("$")
        shopDao.getGood(if (isDyn) id.substring(0, 4) else id) ?: return "{\"error\":430}"

        shopService.consumeGood(user.box, id, 1)
        val useItemResult = useItem(user, id) ?: return "{\"error\":400}"

        val userKKuTuJsonObj = PGobject()
        userKKuTuJsonObj.type = "json"
        userKKuTuJsonObj.value = user.kkutu.toJson()

        val userBoxJsonObj = PGobject()
        userBoxJsonObj.type = "json"
        userBoxJsonObj.value = user.box.toJson()

        userDao.updateUser(user.id, mapOf(
                "\"lastLogin\"" to System.currentTimeMillis(),
                "kkutu" to userKKuTuJsonObj,
                "box" to userBoxJsonObj
        ))

        return if (useItemResult.exp == 0) {
            "{\"result\":200,\"box\":${user.box.toJson()},\"data\":${user.kkutu},\"gain\":[{\"key\":\"${useItemResult.gain.keys.toList()[0]}\",\"value\":${useItemResult.gain.values.toList()[0]}}]}"
        } else {
            "{\"result\":200,\"box\":${user.box.toJson()},\"data\":${user.kkutu},\"gain\":[],\"exp\":${useItemResult.exp}}"
        }
    }

    private fun useItem(user: User, goodId: String): UseItemResult? {
        when (goodId) {
            "boxB2" -> {
                val randomGoodId = listOf("b2_fire", "b2_metal").random()

                shopService.obtainGood(user.box, randomGoodId, 1, 604800)
                return UseItemResult(gain = mapOf(randomGoodId to 1))
            }
            "boxB3" -> {
                val randomGoodId = listOf("b3_do", "b3_hwa", "b3_pok").random()

                shopService.obtainGood(user.box, randomGoodId, 1, 604800)
                return UseItemResult(gain = mapOf(randomGoodId to 1))
            }
            "boxB4" -> {
                val randomGoodId = listOf("b4_bb", "b4_hongsi", "b4_mint").random()

                shopService.obtainGood(user.box, randomGoodId, 1, 604800)
                return UseItemResult(gain = mapOf(randomGoodId to 1))
            }
            "dictPage" -> {
                val objectNode = user.kkutu as ObjectNode

                val addExp = sqrt(1.0 + 2.0 * objectNode.get("score").intValue()).roundToInt()
                objectNode.put("score", objectNode.get("score").intValue() + addExp)

                return UseItemResult(exp = addExp)
            }
            else -> {
                return null
            }
        }
    }
}