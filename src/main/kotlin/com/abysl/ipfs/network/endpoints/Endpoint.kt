package com.abysl.ipfs.network.endpoints

import com.abysl.ipfs.network.config.IpfsClientConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

abstract class Endpoint(endpointUrl: String) {
    protected abstract val config: IpfsClientConfig
    protected val base by lazy { "$config$endpointUrl" }

    companion object {
        val jsonFormat = Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true

        }

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(jsonFormat)
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.NONE
            }
        }

    }
}