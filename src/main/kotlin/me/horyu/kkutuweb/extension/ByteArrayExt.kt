package me.horyu.kkutuweb.extension

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }