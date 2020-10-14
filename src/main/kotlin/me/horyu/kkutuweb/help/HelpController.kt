package me.horyu.kkutuweb.help

import me.horyu.kkutuweb.setting.KKuTuSetting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HelpController(
        @Autowired private val kKuTuSetting: KKuTuSetting,
        @Autowired private val helpService: HelpService
) {
    @GetMapping("/help")
    fun help(model: Model): String {
        model.addAttribute("koInjeongThemes", kKuTuSetting.getKoInjeongThemes())
        model.addAttribute("levels", helpService.getLevels())

        return "help"
    }
}