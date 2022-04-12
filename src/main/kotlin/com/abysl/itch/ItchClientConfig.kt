package com.abysl.itch

data class ItchClientConfig(
    val serverUrl: String = "https://itch.io",
    val apiBaseUrl: String = "/api/1",
    val apiKey: String
) {

    fun apiUrl(): String {
        return "$serverUrl$apiBaseUrl/$apiKey"
    }
}