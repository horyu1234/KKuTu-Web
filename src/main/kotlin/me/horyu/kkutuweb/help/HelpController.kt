package me.horyu.kkutuweb.help

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HelpController(
        @Value("#{'\${kkutu.word.ko.injeong-themes}'.split(',')}") val koInjeongThemes: List<String>,
        @Autowired private val helpService: HelpService
) {
    @GetMapping("/help")
    fun help(model: Model): String {
        model.addAttribute("koInjeongThemes", koInjeongThemes)
        model.addAttribute("levels", helpService.getLevels())

        return "help"
    }
}