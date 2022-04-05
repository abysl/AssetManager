package com.abysl.ipfs.network.endpoints

import com.abysl.ipfs.network.config.IpfsClientConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
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
            install(JsonFeature) {
                serializer = KotlinxSerializer(jsonFormat)
            }
        }
    }
}