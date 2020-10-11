package me.horyu.kkutuweb.game

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class GameClientManager(
        @Value("\${kkutu.game-server.isSecure}") private val isSecure: Boolean,
        @Value("\${kkutu.game-server.host}") private val host: String,
        @Value("#{'\${kkutu.game-server.ports}'.split(',')}") private val ports: List<Int>
) {
    private val logger = LoggerFactory.getLogger(GameClientManager::class.java)
    private val gameClientList = ArrayList<GameClient>()

    @PostConstruct
    fun init() {
        for ((index, port) in ports.withIndex()) {
            gameClientList.add(GameClient(isSecure, host, port, (index + 10).toShort()))
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