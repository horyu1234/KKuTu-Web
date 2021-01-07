package me.horyu.kkutuweb.utils

import me.horyu.kkutuweb.factory.SecondsFactory

object TimeUtils {
    fun getTimeTextForSeconds(seconds: Long): String {
        val timeText = StringBuilder()

        var totalSeconds = seconds
        if (totalSeconds / SecondsFactory.ONE_WEEK > 0) {
            timeText.append(totalSeconds / SecondsFactory.ONE_WEEK).append("주")

            totalSeconds %= SecondsFactory.ONE_WEEK
        }

        if (totalSeconds / SecondsFactory.ONE_DAY > 0) {
            timeText.append(" ").append(totalSeconds / SecondsFactory.ONE_DAY).append("일")

            totalSeconds %= SecondsFactory.ONE_DAY
        }

        if (totalSeconds / SecondsFactory.ONE_HOUR > 0) {
            timeText.append(" ").append(totalSeconds / SecondsFactory.ONE_HOUR).append("시간")

            totalSeconds %= SecondsFactory.ONE_HOUR
        }

        if (totalSeconds / SecondsFactory.ONE_MINUTES > 0) {
            timeText.append(" ").append(totalSeconds / SecondsFactory.ONE_MINUTES).append("분")

            totalSeconds %= SecondsFactory.ONE_MINUTES
        }

        if (totalSeconds > 0) {
            timeText.append(" ").append(totalSeconds).append("초")
        }

        return timeText.toString()
    }
}
