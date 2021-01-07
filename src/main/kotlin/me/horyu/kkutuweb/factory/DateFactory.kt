package me.horyu.kkutuweb.factory

import java.time.format.DateTimeFormatter

object DateFactory {
    val PRETTY_FORMAT = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")!!
    val DATABASE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")!!
}