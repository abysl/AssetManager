package com.abysl.ipfs.network.endpoints

import com.abysl.ipfs.model.IpfsLs
import com.abysl.ipfs.network.config.IpfsClientConfig
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import java.io.File

class Ipfs(override val config: IpfsClientConfig = IpfsClientConfig()) : Endpoint("") {

    val files = Files(this.config)
    val block = Block(this.config)
    val dag = Dag(this.config)

    suspend fun get(
        path: String, output: String? = null, archive: Boolean? = null,
        compress: Boolean? = null, compressionLevel: Int? = null
    ): ByteReadChannel {
        val response: HttpResponse = client.post("$base/get") {
            parameter("arg", path)
            parameter("output", output)
            parameter("archive", archive)
            parameter("compress", compress)
            parameter("compression-level", compressionLevel)
        }
        return response.content
    }

    suspend fun cat(
        path: String
    ): ByteReadChannel {
        val response: HttpResponse = client.post("$base/cat") {
            parameter("arg", path)
        }
        return response.content
    }

    suspend fun ls(
        path: String,
        headers: Boolean? = null,
        resolveType: Boolean? = null,
        size: Boolean? = null,
        stream: Boolean? = null,
    ): IpfsLs {
        val response: HttpResponse = client.post("$base/ls") {
            parameter("arg", path)
            parameter("headers", headers)
            parameter("resolve-type", resolveType)
            parameter("size", size)
            parameter("stream", stream)
        }
        return response.receive()
    }

    suspend fun downloadFile(downloadLocation: File, fileHash: String, overwrite: Boolean = false) {
        if (downloadLocation.exists() && !overwrite) return
        downloadLocation.parentFile.mkdirs()
        val byteReader = cat(fileHash) ?: return
        while (byteReader.availableForRead > 0) {
            val buffer = ByteArray(byteReader.availableForRead)
            byteReader.readAvailable(buffer)
            downloadLocation.writeBytes(buffer)
        }
    }
}