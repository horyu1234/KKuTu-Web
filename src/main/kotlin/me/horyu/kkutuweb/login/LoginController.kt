package me.horyu.kkutuweb.login

import me.horyu.kkutuweb.oauth.VendorType
import me.horyu.kkutuweb.setting.OAuthSetting
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
    @GetMapping
    fun login(model: Model): String {
        model.addAttribute("oAuthSetting", oAuthSetting.getSetting())
        model.addAttribute("viewName", "view/login")
        return "layout"
    }

    @GetMapping("/fail")
    fun loginFailed(model: Model): String {
        model.addAttribute("viewName", "view/loginFailed")
        return "layout"
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

        if (!loginService.login(request, vendorType, code, state)) {
            return "redirect:/login/fail"
        }

        return "redirect:/"
    }
}