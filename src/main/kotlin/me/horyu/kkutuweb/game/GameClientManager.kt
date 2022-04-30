/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2021. horyu1234(admin@horyu.me)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.horyu.kkutuweb.game

import me.horyu.kkutuweb.setting.KKuTuSetting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class GameClientManager(
    @Autowired private val kKuTuSetting: KKuTuSetting
) {
    private val gameClientList = ArrayList<GameClient>()

    @PostConstruct
    fun init() {
        for (gameServer in kKuTuSetting.getGameServers()) {
            gameClientList.add(
                GameClient(
                    gameServer.isSecure,
                    gameServer.host,
                    gameServer.port,
                    gameServer.key,
                    gameServer.cid
                )
            )
        }
    }

    @Scheduled(fixedDelay = 4000)
    fun seek() {
        for (gameClient in gameClientList) {
            gameClient.send("{\"type\":\"seek\"}")
        }
    }

    fun kick(userId: String, ip: String) {
        for (gameClient in gameClientList) {
            gameClient.send("{\"type\":\"kick\",\"userId\":\"$userId\",\"ip\": \"$ip\"}")
        }
    }

    fun getPlayers(): List<Int?> {
        return gameClientList.map {
            if (it.isConnected()) it.players else null
        }
    }

    fun yell(value: String) {
        for (gameClient in gameClientList) {
            gameClient.send("{\"type\":\"yell\",\"value\":$value}")
        }
    }
}