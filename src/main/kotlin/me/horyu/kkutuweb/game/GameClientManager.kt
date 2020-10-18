package me.horyu.kkutuweb.game

import me.horyu.kkutuweb.setting.KKuTuSetting
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class GameClientManager(
        @Autowired private val kKuTuSetting: KKuTuSetting
) {
    private val logger = LoggerFactory.getLogger(GameClientManager::class.java)
    private val gameClientList = ArrayList<GameClient>()

    @PostConstruct
    fun init() {
        for (gameServer in kKuTuSetting.getGameServers()) {
            gameClientList.add(GameClient(gameServer.isSecure, gameServer.host, gameServer.port, gameServer.cid))
        }
    }

    @Scheduled(fixedDelay = 4000)
    fun seek() {
        for (gameClient in gameClientList) {
            gameClient.send("{\"type\":\"seek\"}")
        }
    }

    fun getPlayers(): List<Int?> {
        return gameClientList.map {
            if (it.isConnected()) it.players else null
        }
    }
}