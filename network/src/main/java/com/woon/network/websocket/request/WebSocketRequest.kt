package com.woon.network.websocket.request

import com.google.gson.Gson
import java.util.UUID

class WebSocketRequest
private constructor(
    val url: String,
    val codes: List<String>,
    val type: String = "ticker",
    val ticket: String = UUID.randomUUID().toString(),
    val format: String = "DEFAULT"
) {

    fun toSubscribeMessage(): String {
        val message = listOf(
            mapOf("ticket" to ticket),
            mapOf("type" to type, "codes" to codes),
            mapOf("format" to format)
        )
        return Gson().toJson(message)
    }

    class Builder {
        private lateinit var url: String
        private lateinit var codes: List<String>
        private var ticket: String = UUID.randomUUID().toString()
        private var format: String = "DEFAULT"
        private var type: String = "ticker"

        fun url(url: String) = apply { this.url = url }
        fun codes(codes: List<String>) = apply { this.codes = codes }
        fun type(type: String) = apply { this.type = type }
        fun ticket(ticket: String) = apply { this.ticket = ticket }
        fun format(format: String) = apply { this.format = format }

        fun build(): WebSocketRequest {
            return WebSocketRequest(
                url,
                codes,
                type,
                ticket,
                format
            )
        }
    }
}
