package me.horyu.kkutuweb.consume

data class UseItemResult(
        val exp: Int = 0,
        val gain: Map<String, Int> = HashMap()
)