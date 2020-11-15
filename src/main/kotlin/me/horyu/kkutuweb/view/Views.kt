/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2020. horyu1234(admin@horyu.me)
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

package me.horyu.kkutuweb.view

import org.springframework.http.HttpHeaders
import javax.servlet.http.HttpServletRequest

object Views {
    private val viewMap = mapOf(
            View.LAYOUT to DesktopAndMobile("layout", "m_layout"),
            View.PORTAL to DesktopAndMobile("view/portal", "view/portal"),
            View.KKUTU to DesktopAndMobile("view/kkutu/kkutu", "view/kkutu/m_kkutu")
    )

    fun HttpServletRequest.getView(view: View): String {
        val desktopAndMobile = viewMap[view] ?: error("${view.name} 를 ViewMap 에서 찾을 수 없습니다.")

        val userAgent = this.getHeader(HttpHeaders.USER_AGENT)
        return if (userAgent != null && userAgent.toLowerCase().contains("mobile")) {
            desktopAndMobile.mobile
        } else {
            desktopAndMobile.desktop
        }
    }
}