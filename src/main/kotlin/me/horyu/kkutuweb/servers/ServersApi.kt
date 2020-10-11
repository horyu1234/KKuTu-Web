package me.horyu.kkutuweb.servers

import me.horyu.kkutuweb.game.GameClientManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ServersApi(
        @Autowired private val gameClientManager: GameClientManager,
        @Value("\${kkutu.max-players}") val maxPlayers: Int
) {
    @GetMapping("/servers")
    fun getServers(): ServersResponse {
        return ServersResponse(gameClientManager.getPlayers(), maxPlayers)
    }
}