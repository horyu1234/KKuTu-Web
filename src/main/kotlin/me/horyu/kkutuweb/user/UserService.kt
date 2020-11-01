/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2020. horyu1234(admin@horyu.me)
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

package me.horyu.kkutuweb.user

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import me.horyu.kkutuweb.extension.getOAuthUser
import me.horyu.kkutuweb.extension.isGuest
import me.horyu.kkutuweb.extension.toJson
import me.horyu.kkutuweb.session.SessionDao
import me.horyu.kkutuweb.shop.ShopDao
import me.horyu.kkutuweb.shop.ShopService
import org.postgresql.util.PGobject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpSession
import kotlin.math.roundToInt

private val AVAIL_EQUIP = listOf(
        "NIK", "BDG1", "BDG2", "BDG3", "BDG4",
        "Mhead", "Meye", "Mmouth", "Mhand", "Mclothes", "Mshoes", "Mback"
)

@Service
class UserService(
        @Autowired private val objectMapper: ObjectMapper,
        @Autowired private val sessionDao: SessionDao,
        @Autowired private val userDao: UserDao,
        @Autowired private val shopDao: ShopDao,
        @Autowired private val shopService: ShopService
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun getBox(session: HttpSession): String {
        if (session.isGuest()) return "{\"error\":400}"
        val oAuthUser = session.getOAuthUser()

        val userId = "${oAuthUser.vendorType.name.toLowerCase()}-${oAuthUser.vendorId}"
        val user = userDao.getUser(userId) ?: return "{\"error\":400}"

        return user.box.toJson()
    }

    fun exordial(data: String, nick: String, session: HttpSession): String {
        val maxExordialLength = 100
        val nickRegex = "^[ㄱ-ㅎㅏ-ㅣ가-힣A-Za-z0-9-_\\s]{1,20}$".toRegex()

        if (session.isGuest()) return "{\"error\":400}"

        val oAuthUser = session.getOAuthUser()
        val userId = "${oAuthUser.vendorType.name.toLowerCase()}-${oAuthUser.vendorId}"

        val resultData = data.substring(0, if (data.length > maxExordialLength) maxExordialLength else data.length).trim()
        val resultNick = nick.trim()

        if (!nickRegex.matches(resultNick)) return "{\"error\":400}"

        userDao.updateUser(userId, mapOf(
                "exordial" to if (resultData.isEmpty()) null else resultData,
                "nickname" to resultNick
        ))
        sessionDao.updateNickname(userId, resultNick)

        logger.info("$userId 님이 프로필을 수정했습니다. 별명: $resultNick  /  소개 한마디: $resultData")
        return "{\"text\":\"$resultData\"}"
    }

    fun equip(id: String, isLeft: Boolean, session: HttpSession): String {
        if (session.isGuest()) return "{\"error\":400}"
        val oAuthUser = session.getOAuthUser()

        val userId = "${oAuthUser.vendorType.name.toLowerCase()}-${oAuthUser.vendorId}"
        val user = userDao.getUser(userId) ?: return "{\"error\":400}"

        val good = shopDao.getGood(id) ?: return "{\"error\":430}"
        if (!AVAIL_EQUIP.contains(good.group)) return "{\"error\":400}"

        var part = good.group
        if (part.substring(0, 3) == "BDG") part = "BDG"
        else if (part == "Mhand") part = if (isLeft) "Mlhand" else "Mrhand"

        val equip: JsonNode = user.equip
        if (equip.has(part)) {
            val equipingGood = user.box.get(id)
            if (equipingGood != null && (equipingGood.has("expire") && equipingGood.get("expire").intValue() > 0)) {
                shopService.obtainGood(user.box, equip.get(part).textValue(), 1, equipingGood.get("expire").intValue(), true)
            } else {
                val currentTime = (System.currentTimeMillis() * 0.001).roundToInt()
                shopService.obtainGood(user.box, equip.get(part).textValue(), 1, currentTime + good.term, true)
            }
        }

        val equipObjectNode = equip as ObjectNode
        if (equip.has(part) && equip.get(part).textValue() == good.id) {
            equipObjectNode.remove(part)
        } else {
            if (!user.box.has(good.id)) return "{\"error\":430}"
            shopService.consumeGood(user.box, good.id, 1)
            equipObjectNode.put(part, good.id)
        }

        val userBoxJsonObj = PGobject()
        userBoxJsonObj.type = "json"
        userBoxJsonObj.value = user.box.toJson()

        val userEquipJsonObj = PGobject()
        userEquipJsonObj.type = "json"
        userEquipJsonObj.value = user.equip.toJson()

        userDao.updateUser(user.id, mapOf(
                "box" to userBoxJsonObj,
                "equip" to userEquipJsonObj
        ))
        return "{\"result\":200,\"box\":${user.box.toJson()},\"equip\":${user.equip.toJson()}}"
    }
}