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