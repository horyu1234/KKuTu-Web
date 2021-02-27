package me.horyu.kkutuweb.utils

import java.util.*

object WordUtils {
    fun serializeMean(means: List<String>): String {
        val resultText = StringBuilder()

        var count = 0
        means.forEach { mean ->
            count++
            resultText.append("＂").append(count).append("＂ ").append(mean).append("  ")
        }

        return resultText.toString()
    }

    fun deserializeMean(mean: String): List<String> {
        val dbMeans = ArrayList(listOf(*mean.split("＂[0-9]+＂".toRegex()).toTypedArray()))
        if (dbMeans.isEmpty()) return emptyList()

        dbMeans.removeAt(0)
        return dbMeans.map { if (it.contains("［")) "" else it }
    }
}
