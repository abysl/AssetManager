package com.abysl.itch

data class ItchClientConfig(
    val apiKey: String,
    val serverUrl: String = "https://itch.io",
    val apiBaseUrl: String = "/api/1",
) {

    fun apiUrl(): String {
        return "$serverUrl$apiBaseUrl/$apiKey"
    }
}