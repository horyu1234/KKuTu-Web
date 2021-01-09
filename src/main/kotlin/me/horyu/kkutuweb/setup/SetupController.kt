/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2021. horyu1234(admin@horyu.me)
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

package me.horyu.kkutuweb.setup

import me.horyu.kkutuweb.extension.getIp
import me.horyu.kkutuweb.extension.isGuest
import me.horyu.kkutuweb.login.LoginService
import me.horyu.kkutuweb.view.View
import me.horyu.kkutuweb.view.Views.getView
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/setup")
class SetupController(
    @Autowired private val loginService: LoginService,
    @Autowired private val setupService: SetupService
) {
    private val logger = LoggerFactory.getLogger(SetupController::class.java)

    @GetMapping
    fun setup(
        model: Model,
        request: HttpServletRequest,
        session: HttpSession
    ): String {
        val isGuest = session.isGuest()
        val sessionProfile = loginService.getSessionProfile(session)

        if (isGuest || !setupService.needSetup(sessionProfile!!)) {
            return "redirect:/"
        }

        model.addAttribute("viewName", request.getView(View.REACT))

        logger.info("[${request.getIp()}] 초기 설정 화면에 접속했습니다.")
        return request.getView(View.LAYOUT)
    }
}