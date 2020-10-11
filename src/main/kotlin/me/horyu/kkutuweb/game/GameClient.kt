package me.horyu.kkutuweb.game

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.net.URI

class GameClient(
        private val isSecure: Boolean,
        private val host: String,
        private val port: Int,
        private val id: Short
) : TextWebSocketHandler() {
    private val logger = LoggerFactory.getLogger(GameClient::class.java)
    private var clientSession: WebSocketSession? = null
    var players = 0

    init {
        connectWebSocket()
    }

    fun connectWebSocket() {
        try {
            val protocol = if (isSecure) "wss" else "ws"

            val webSocketClient = StandardWebSocketClient()
            clientSession = webSocketClient.doHandshake(this, WebSocketHttpHeaders(), URI.create("$protocol://$host:$port/$id")).get()
        } catch (e: Exception) {
            logger.warn("$port @ 게임서버 연결에 실패했습니다. ${e.message}")
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("$port @ 게임서버#${id} 가 연결되었습니다.")
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.info("$port @ 게임서버#${id} 에서 오류가 발생했습니다. $exception")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info("$port @ 게임서버#${id} 의 연결이 끊어졌습니다. $status")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val data = message.payload

        val jsonNode = ObjectMapper().readTree(data)
        val type = jsonNode.get("type").textValue()

        if (type == "seek") {
            players = jsonNode.get("value").intValue()
        }
    }

    fun send(data: String) {
        if (!isConnected()) return
        clientSession!!.sendMessage(TextMessage(data))
    }

    fun isConnected(): Boolean {
        return clientSession != null && clientSession!!.isOpen
    }
}