package me.horyu.kkutuweb.extension

import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.getIp(): String {
    val cfHeader = this.getHeader("HTTP_CF_CONNECTING_IP")
    val forwardedForHeader = this.getHeader("X-FORWARDED-FOR")

    return when {
        cfHeader != null -> {
            if (cfHeader.contains(",")) {
                cfHeader.split(",")[0].trim()
            } else {
                cfHeader
            }
        }
        forwardedForHeader != null -> {
            if (forwardedForHeader.contains(",")) {
                forwardedForHeader.split(",")[0].trim()
            } else {
                forwardedForHeader
            }
        }
        else -> {
            this.remoteAddr
        }
    }
}