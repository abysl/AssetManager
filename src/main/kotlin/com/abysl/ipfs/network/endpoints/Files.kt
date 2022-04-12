package com.abysl.ipfs.network.endpoints

import com.abysl.ipfs.model.IpfsFile
import com.abysl.ipfs.model.IpfsPath
import com.abysl.ipfs.network.config.IpfsClientConfig
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*

class Files(override val config: IpfsClientConfig): Endpoint("/files") {

    suspend fun stat(path: String): IpfsFile {
        val ipfsPath = IpfsPath(path)
        val response: HttpResponse = client.post("$base/stat") {
            parameter("arg", ipfsPath)
        }
        return response.body()
    }

    suspend fun read(path: String, offset: ULong? = null, count: ULong? = null): ByteReadChannel {
        val ipfsPath = IpfsPath(path)
        val response: HttpResponse = client.post("$base/read") {
            parameter("arg", ipfsPath)
            parameter("offset", offset)
            parameter("count", count)
        }
        return response.body()
    }

    suspend fun writeFile(
        path: String,
        fileData: ByteArray,
        create: Boolean = true,
        parents: Boolean = true,
        overwrite: Boolean = false,
    ) {
        val ipfsPath = IpfsPath(path)
        val response: HttpResponse = client.submitFormWithBinaryData(
            url = "$base/write?arg=$ipfsPath",
            formData = formData { append("file", fileData) }
        ) {
            parameter("create", create)
            parameter("parents", parents)
            parameter("truncate", overwrite)
            headers {
                append(HttpHeaders.Accept, "multipart/form-data")
            }
        }
    }
}