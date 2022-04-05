package com.abysl.ipfs.network.config

data class IpfsClientConfig(
    val serverUrl: String = "http://127.0.0.1",
    val serverPort: Int = 5001,
    val apiBaseUrl: String = "/api/v0"
) {

    override fun toString(): String {
        return "$serverUrl:$serverPort$apiBaseUrl"
    }
}