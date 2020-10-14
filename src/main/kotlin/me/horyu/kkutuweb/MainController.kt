package me.horyu.kkutuweb

import me.horyu.kkutuweb.extension.getOAuthUser
import me.horyu.kkutuweb.extension.isGuest
import me.horyu.kkutuweb.session.SessionDao
import me.horyu.kkutuweb.session.SessionProfile
import me.horyu.kkutuweb.setting.KKuTuSetting
import me.horyu.kkutuweb.user.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*
import javax.servlet.http.HttpSession
import kotlin.streams.asSequence

@Controller
class MainController(
        @Autowired private val kKuTuSetting: KKuTuSetting,
        @Autowired private val sessionDao: SessionDao,
        @Autowired private val userDao: UserDao,
        @Autowired private val aeS256: AES256
) {
    @GetMapping
    fun main(@RequestParam(required = false) server: Short?,
             model: Model, session: HttpSession): String {
        if (server == null) {
            model.addAttribute("viewName", "view/portal")
        } else {
            val randomSid = generateRandomSid()

            if (!session.isGuest()) {
                val oAuthUser = session.getOAuthUser()

                val userId = "${oAuthUser.vendorType.name.toLowerCase()}-${oAuthUser.vendorId}"
                val user = userDao.getUser(userId)

                val authType = oAuthUser.vendorType.name.toLowerCase()
                val title = if (user == null) oAuthUser.name else (user.nickname ?: oAuthUser.name)

                sessionDao.insert(SessionProfile(
                        authType = authType,
                        id = authType + "-" + oAuthUser.vendorId,
                        name = oAuthUser.name,
                        title = title,
                        image = oAuthUser.profileImage
                ), randomSid)
            }

            model.addAttribute("websocketUrl", "wss://test.kkutu.io:21000/" + aeS256.encrypt(randomSid))
            model.addAttribute("moremiParts", kKuTuSetting.getMoremiParts())
            model.addAttribute("moremiEquips", kKuTuSetting.getMoremiEquips())

            model.addAttribute("viewName", "view/kkutu")
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