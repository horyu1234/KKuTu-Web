package me.horyu.kkutuweb.view

import org.springframework.http.HttpHeaders
import javax.servlet.http.HttpServletRequest

object Views {
    private val viewMap = mapOf(
            View.LAYOUT to DesktopAndMobile("layout", "m_layout"),
            View.PORTAL to DesktopAndMobile("view/portal", "view/m_portal")
    )

    fun HttpServletRequest.getView(view: View): String {
        val desktopAndMobile = viewMap[view] ?: error("${view.name} 를 ViewMap 에서 찾을 수 없습니다.")

        val userAgent = this.getHeader(HttpHeaders.USER_AGENT)
        return if (userAgent.toLowerCase().contains("mobile")) {
            desktopAndMobile.mobile
        } else {
            desktopAndMobile.desktop
        }
    }
}