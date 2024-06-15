package com.d3cima.tvgame.server

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object Server {
    private var onConnectAction: () -> Unit = {}
    private var connected = false
    private var token: String? = null
    private var webSocketClient: WebsocketClient? = null

    @Serializable
    data class NewGame(val hub: String)

    init {
        GlobalScope.launch {
            val client = HttpClient() {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                }
            }.use { client ->


                val response: HttpResponse = client.get("https://quizapp.henri.run/new") {}
                val newHub: NewGame = response.body()
                token = newHub.hub
                webSocketClient = WebsocketClient("wss://quizapp.henri.run/" + token + "/ws")

                webSocketClient!!.connect(listener = object : WebSocketListener {
                    override fun onConnected() {
                        connected = true
                        onConnectAction()
                    }

                    override fun onMessage(message: String) {
                        //do nothing
                    }

                    override fun onDisconnected() {
                        connected = false
                    }

                })

            }

        }

    }

    fun onConnect(callable: () -> Unit) {
        onConnectAction = callable
    }

    fun ws(): WebsocketClient? {
        return webSocketClient
    }

    fun token(): String {
        return token ?: "NO_TOKEN YET"
    }

    fun isConnected(): Boolean {
        return connected
    }


}