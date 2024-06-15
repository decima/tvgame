package com.d3cima.tvgame.server

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.ws
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.serialization.sendSerializedBase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


class WebsocketClient(private val url: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    @Serializable
    data class Action(val action: String, val payload: Any)

    private var socketSession: DefaultClientWebSocketSession? = null


    fun connect(listener: WebSocketListener) {
        GlobalScope.launch {
            client.ws(url) {
                listener.onConnected()
                socketSession = this

                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            listener.onMessage(frame.readText())
                        }
                    }
                } catch (e: Exception) {
                    listener.onDisconnected()
                }
            }
        }
    }

    fun disconnect() {
        client.close()
    }


    suspend fun emit(action: Action) {
        socketSession!!.sendSerialized(action)

    }


}