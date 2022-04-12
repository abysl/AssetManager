package com.abysl.ipfs.network.endpoints

import com.abysl.ipfs.model.IpfsDag
import com.abysl.ipfs.network.config.IpfsClientConfig
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import kotlinx.serialization.decodeFromString

class Dag(override val config: IpfsClientConfig): Endpoint("/dag") {
    suspend fun get(hash: String): IpfsDag {
        val response: HttpResponse = client.post("$base/get") {
            parameter("arg", hash)
        }
        val jsonString = response.bodyAsText()
        return jsonFormat.decodeFromString(jsonString)
    }
}