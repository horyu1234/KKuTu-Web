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

package me.horyu.kkutuweb.help

import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import kotlin.math.floor
import kotlin.math.roundToLong

private const val MAX_LEVEL = 720

@Service
class HelpService {
    private val levels = ArrayList<LevelInfo>()

    @PostConstruct
    fun init() {
        var totalExp = 0L
        for (lv in 1..MAX_LEVEL) {
            val requiredScore = getRequiredExp(lv)
            totalExp += requiredScore

            levels.add(LevelInfo(
                    level = lv,
                    requireExp = if (lv == MAX_LEVEL) null else requiredScore,
                    totalExp = if (lv == MAX_LEVEL) null else totalExp
            ))
        }
    }

    fun getRequiredExp(lv: Int): Long {
        val perLevel5Multiplier: Float = if (lv % 5 == 0) 0.3F + 1F else 1F
        val perLevel15Multiplier: Float = if (lv % 15 == 0) 0.4F + 1F else 1F
        val perLevel45Multiplier: Float = if (lv % 45 == 0) 0.5F + 1F else 1F
        val totalMultiplier = perLevel5Multiplier * perLevel15Multiplier * perLevel45Multiplier

        return when {
            lv <= 240 -> (totalMultiplier * (
                    120 + floor(lv / 5.0) * 60 + floor(lv * lv / 225.0) * 120 + floor(lv * lv / 2025.0) * 180
                    )).roundToLong()
            lv <= 480 -> (totalMultiplier * (
                    120 + floor(lv / 5.0) * 100 + floor(lv * lv / 225.0) * 170 + floor(lv * lv / 2025.0) * 240
                    )).roundToLong()
            else -> (totalMultiplier * (
                    120 + floor(lv / 5.0) * 140 + floor(lv * lv / 225.0) * 220 + floor(lv * lv / 2025.0) * 300
                    )).roundToLong()
        }
    }

    fun getLevels() = this.levels
}