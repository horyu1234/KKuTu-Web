package me.horyu.kkutuweb

import me.horyu.kkutuweb.locale.LocalePropertyLoader
import me.horyu.kkutuweb.login.LoginService
import me.horyu.kkutuweb.session.SessionDao
import me.horyu.kkutuweb.setting.KKuTuSetting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.support.RequestContextUtils
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import kotlin.streams.asSequence

@Controller
class MainController(
        @Autowired private val kKuTuSetting: KKuTuSetting,
        @Autowired private val loginService: LoginService,
        @Autowired private val sessionDao: SessionDao,
        @Autowired private val aeS256: AES256,
        @Autowired private val localePropertyLoader: LocalePropertyLoader
) {
    @GetMapping
    fun main(@RequestParam(required = false) server: Short?,
             model: Model, session: HttpSession, request: HttpServletRequest): String {
        if (server == null) {
            model.addAttribute("viewName", "view/portal")
        } else {
            val randomSid = generateRandomSid()

            val sessionProfile = loginService.getSessionProfile(session)
            if (sessionProfile != null) {
                sessionDao.insert(sessionProfile, randomSid)
            }

            val locale = RequestContextUtils.getLocale(request)
            val messages = localePropertyLoader.getMessages(locale)

            val gameServers = kKuTuSetting.getGameServers()
            val gameServer = gameServers[if (gameServers.size <= server) 0 else server.toInt()]
            val webSocketUrl = (if (gameServer.isSecure) "wss" else "ws") + "://" + gameServer.publicHost + ":" + gameServer.port

            model.addAttribute("version", kKuTuSetting.getVersion())
            model.addAttribute("websocketUrl", webSocketUrl + "/" + aeS256.encrypt(randomSid))
            model.addAttribute("nickname", sessionProfile?.title ?: messages["kkutu.dialog.room.room-title.guest"])
            model.addAttribute("moremiParts", kKuTuSetting.getMoremiParts().joinToString(","))
            model.addAttribute("moremiCategories", kKuTuSetting.getMoremiCategories())
            model.addAttribute("moremiEquips", kKuTuSetting.getMoremiEquips().joinToString(","))
            model.addAttribute("moremiGroups", kKuTuSetting.getMoremiGroups())
            model.addAttribute("gameRules", kKuTuSetting.getGameRules())
            model.addAttribute("gameOptions", kKuTuSetting.getGameOptions())
            model.addAttribute("gameOptionMap", kKuTuSetting.getGameOptionMap())
            model.addAttribute("gameModes", kKuTuSetting.getGameModes())

            val injeongPickExcepts = kKuTuSetting.getInjeongPickExcepts()
            val koThemes = ArrayList<String>()
            koThemes.addAll(kKuTuSetting.getKoThemes())
            koThemes.addAll(kKuTuSetting.getKoInjeongThemes())
            koThemes.removeAll(injeongPickExcepts)

            val enThemes = ArrayList<String>()
            enThemes.addAll(kKuTuSetting.getEnThemes())
            enThemes.addAll(kKuTuSetting.getEnInjeongThemes())
            enThemes.removeAll(injeongPickExcepts)

            model.addAttribute("koThemes", koThemes)
            model.addAttribute("enThemes", enThemes)

            model.addAttribute("viewName", "view/kkutu/kkutu")
        }

        return "layout"
    }

    fun generateRandomSid(): String {
        val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return Random().ints(32, 0, source.length)
                .asSequence()
                .map(source::get)
                .joinToString("")
    }
}