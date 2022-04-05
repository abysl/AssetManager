package com.abysl.ipfs.network.endpoints

import com.abysl.ipfs.network.config.IpfsClientConfig
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*

class Block(override val config: IpfsClientConfig): Endpoint("/block") {

    suspend fun getBlock(hash: String): ByteReadChannel {
        val response: HttpResponse = client.get("$base/get") {
            parameter("arg", hash)
        }
        return response.content
    }
}