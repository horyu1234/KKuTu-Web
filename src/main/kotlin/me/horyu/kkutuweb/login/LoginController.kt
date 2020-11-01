package me.horyu.kkutuweb.login

import me.horyu.kkutuweb.extension.getIp
import me.horyu.kkutuweb.extension.getOAuthUser
import me.horyu.kkutuweb.oauth.VendorType
import me.horyu.kkutuweb.setting.OAuthSetting
import me.horyu.kkutuweb.view.View
import me.horyu.kkutuweb.view.Views.getView
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/login")
class LoginController(
        @Autowired private val oAuthSetting: OAuthSetting,
        @Autowired private val loginService: LoginService
) {
    private val logger = LoggerFactory.getLogger(LoginController::class.java)

    @GetMapping
    fun login(model: Model,
              request: HttpServletRequest): String {
        val isMobile = model.getAttribute("mobile") as Boolean
        val mobileLogText = if (isMobile) " (모바일)" else ""
        logger.info("[${request.getIp()}] 로그인 화면을 요청했습니다.$mobileLogText")

        model.addAttribute("oAuthSetting", oAuthSetting.getSetting())
        model.addAttribute("viewName", "view/login")
        return request.getView(View.LAYOUT)
    }

    @GetMapping("/fail")
    fun loginFailed(model: Model,
                    request: HttpServletRequest): String {
        model.addAttribute("viewName", "view/loginFailed")
        return request.getView(View.LAYOUT)
    }

    @GetMapping("/logout")
    fun logout(session: HttpSession): String {
        try {
            session.invalidate()
        } catch (e: Exception) {
        }
        return "redirect:/"
    }

    @GetMapping("/{vendorName}")
    fun loginRequest(@PathVariable vendorName: String,
                     session: HttpSession): String {
        val vendorType = VendorType.fromName(vendorName) ?: return "redirect:/login/fail"

        return "redirect:" + loginService.getAuthorizationUrl(session, vendorType)
    }

    @GetMapping("/{vendorName}/callback")
    fun loginCallback(@PathVariable vendorName: String,
                      @RequestParam("code") code: String,
                      @RequestParam("state") state: String,
                      request: HttpServletRequest): String {
        val vendorType = VendorType.fromName(vendorName) ?: return "redirect:/login/fail"

        val loginSuccess = loginService.login(request, vendorType, code, state)
        if (loginSuccess) {
            val session = request.session
            val oAuthUser = session.getOAuthUser()

            logger.info("${oAuthUser.name}(${oAuthUser.vendorId}) 님이 ${vendorType.name} 로그인에 성공했습니다.")
        } else {
            logger.info("${request.session.id} 세션에서 ${vendorType.name} 로그인에 실패했습니다.")
        }

        return if (!loginSuccess) "redirect:/login/fail"
        else "redirect:/"
    }
}