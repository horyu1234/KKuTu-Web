package me.horyu.kkutuweb.extension

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toTimestamp() = Timestamp.from(this.toInstant(ZoneOffset.ofHours(9)))!!
