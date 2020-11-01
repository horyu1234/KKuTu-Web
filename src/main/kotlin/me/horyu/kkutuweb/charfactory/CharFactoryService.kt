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

package me.horyu.kkutuweb.charfactory

import com.fasterxml.jackson.databind.ObjectMapper
import me.horyu.kkutuweb.dict.WordDao
import me.horyu.kkutuweb.extension.getOAuthUser
import me.horyu.kkutuweb.extension.isGuest
import me.horyu.kkutuweb.shop.ShopService
import me.horyu.kkutuweb.user.UserDao
import org.postgresql.util.PGobject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpSession
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random

@Service
class CharFactoryService(
        @Autowired private val objectMapper: ObjectMapper,
        @Autowired private val userDao: UserDao,
        @Autowired private val wordDao: WordDao,
        @Autowired private val shopService: ShopService
) {
    fun previewCharFactory(word: String, l: Int, b: String, session: HttpSession): CFResult {
        return getCfRewards(word, l, b == "1")
    }

    fun charFactory(tray: List<String>, session: HttpSession): String {
        if (tray.isEmpty() || tray.size > 6) return "{\"error\":400}"

        if (session.isGuest()) return "{\"error\":400}"
        val oAuthUser = session.getOAuthUser()

        val userId = "${oAuthUser.vendorType.name.toLowerCase()}-${oAuthUser.vendorId}"
        val user = userDao.getUser(userId) ?: return "{\"error\":400}"

        var wordString = ""
        var level = 0
        val charCountMap = HashMap<String, Int>()

        for (charItem in tray) {
            val char = charItem[4].toString()
            wordString += char
            level += 68 - charItem[3].toInt()
            charCountMap[charItem] = if (charCountMap.containsKey(charItem)) charCountMap[charItem]!! + 1 else 1
            if (!user.box.has(charItem) || user.box.get(charItem).intValue() < charCountMap[charItem]!!) return "{\"error\":434}"
        }

        val word = when (findLanguage(wordString)) {
            "ko" -> {
                wordDao.getKoreanWord(wordString)
            }
            "en" -> {
                wordDao.getEnglishWord(wordString)
            }
            else -> {
                null
            }
        }

        var blend = false
        if (word == null) {
            if (wordString.length == 3) {
                blend = true
            } else return "{\"error\":404}"
        }

        val cfRewards = getCfRewards(wordString, level, blend)
        if (user.money < cfRewards.cost) return "{\"error\":407}"

        for (entry in charCountMap.entries) {
            shopService.consumeGood(user.box, entry.key, entry.value)
        }

        val gained = ArrayList<Reward>()
        for (reward in cfRewards.data) {
            if (Random.nextDouble(0.0, 1.0) >= reward.rate) continue
            if (reward.key[4].toString() == "?") {
                reward.key = reward.key.substring(0, 4) + if (blend) blendWord(wordString) else wordString.random().toString()
            }

            shopService.obtainGood(user.box, reward.key, reward.value, null)
            gained.add(reward)
        }

        val afterMoney = user.money - cfRewards.cost

        val userBoxJsonObj = PGobject()
        userBoxJsonObj.type = "json"
        userBoxJsonObj.value = objectMapper.writeValueAsString(user.box)

        userDao.updateUser(user.id, mapOf(
                "money" to afterMoney,
                "box" to userBoxJsonObj
        ))
        return "{\"result\":200,\"box\":${objectMapper.writeValueAsString(user.box)},\"money\":$afterMoney,\"gain\":${objectMapper.writeValueAsString(gained)}}"
    }

    fun blendWord(word: String): String {
        val lang = findLanguage(word)
        val choseongList = HashSet<Int>()
        val jungseongList = HashSet<Int>()
        val jongseongList = HashSet<Int>()

        return when (lang) {
            "en" -> {
                "abcdefghijklmnopqrstuvwxyz".toCharArray().random().toString()
            }
            "ko" -> {
                for (i in word.indices) {
                    val char = word[i].toInt() - 0xAC00

                    choseongList.add(char / 28 / 21)
                    jungseongList.add((char / 28) % 21)
                    jongseongList.add(char % 28)
                }

                (((choseongList.shuffled()[0] * 21) + jungseongList.shuffled()[0]) * 28 + jongseongList.shuffled()[0] + 0xAC00).toChar().toString()
            }
            else -> {
                "ERROR"
            }
        }
    }

    fun findLanguage(word: String): String {
        return if ("[a-zA-Z]".toRegex().containsMatchIn(word)) "en" else "ko"
    }

    fun getCfRewards(word: String, level: Int, blend: Boolean): CFResult {
        val wordLength = word.length

        var cost = 20 * level
        var wur = wordLength / 36.0

        val rewards = ArrayList<Reward>()
        if (blend) {
            when {
                wur >= 0.5 -> {
                    rewards.add(Reward("\$WPA?", 1, 1.0))
                }
                wur >= 0.35 -> {
                    rewards.add(Reward("\$WPB?", 1, 1.0))
                }
                wur >= 0.05 -> {
                    rewards.add(Reward("\$WPC?", 1, 1.0))
                }
            }
            cost = (cost * 0.2).roundToInt()
        } else {
            rewards.add(Reward("dictPage", (wordLength * 0.6).roundToInt(), 1.0))
            rewards.add(Reward("boxB4", 1, min(1.0, level / 7.0)))
            if (level >= 5) {
                rewards.add(Reward("boxB3", 1, min(1.0, level / 15.0)))
                cost += 10 * level
                wur += level / 20.0
            }
            if (level >= 10) {
                rewards.add(Reward("boxB2", 1, min(1.0, level / 30.0)))
                cost += 20 * level
                wur += level / 10.0
            }
            if (wur >= 0.05) {
                if (wur > 1) rewards.add(Reward("\$WPC?", floor(wur).toInt(), 1.0))
                rewards.add(Reward("\$WPC?", 1, wur % 1))
            }
            if (wur >= 0.35) {
                if (wur > 2) rewards.add(Reward("\$WPB?", floor(wur / 2).toInt(), 1.0))
                rewards.add(Reward("\$WPB?", 1, (wur / 2.0) % 1))
            }
            if (wur >= 0.5) {
                rewards.add(Reward("\$WPA?", 1, wur / 3.0))
            }
        }

        return CFResult(cost, rewards)
    }
}