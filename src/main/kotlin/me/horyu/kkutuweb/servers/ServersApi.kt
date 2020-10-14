package me.horyu.kkutuweb.servers

import me.horyu.kkutuweb.game.GameClientManager
import me.horyu.kkutuweb.setting.KKuTuSetting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ServersApi(
        @Autowired private val kKuTuSetting: KKuTuSetting,
        @Autowired private val gameClientManager: GameClientManager
) {
    @GetMapping("/servers")
    fun getServers(): ServersResponse {
        return ServersResponse(gameClientManager.getPlayers(), kKuTuSetting.getMaxPlayer())
    }
}