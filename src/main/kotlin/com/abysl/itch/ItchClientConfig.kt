package com.abysl.itch

data class ItchClientConfig(
    //https://itch.io/api/1/KEY/game/GAME_ID/purchases
    val serverUrl: String = "https://itch.io",
    val apiBaseUrl: String = "/api/1",
    val apiKey: String
) {

    fun apiUrl(): String {
        return "$serverUrl$apiBaseUrl/$apiKey"
    }
}