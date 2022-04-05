package com.abysl.ipfs.network.endpoints

import com.abysl.ipfs.model.IpfsLs
import com.abysl.ipfs.network.config.IpfsClientConfig
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import okio.ByteString.Companion.encodeUtf8
import java.io.File

class Ipfs(override val config: IpfsClientConfig = IpfsClientConfig()) : Endpoint("") {

    val files = Files(this.config)
    val block = Block(this.config)
    val dag = Dag(this.config)

    private fun getFormPart(file: File): FormPart<Any> {
        if (file.isDirectory) {
            return FormPart(
                "application/x-directory", "",
                headers = Headers.build {
                    append(
                        "Content-Disposition",
                        "form-data; name=\"${file.name.encodeUtf8()}\"; filename=\"${file.path.encodeUtf8()}\""
                    )
                    append("Content-Type", "application/x-directory")
                }
            )
        } else {
            return FormPart(
                key = "application/octet-stream", file.readBytes(),
                Headers.build {
                    append("Abspath", "${file.absolutePath.encodeUtf8()}")
                    append(
                        "ContentDisposition",
                        "form-data; name=\"${file.name.encodeUtf8()}\"; filename=\"${file.parentFile.name.encodeUtf8()}%2F${file.name.encodeUtf8()}\""
                    )
                    append("Content-Type", "application/octet-stream")
                }
            )
        }
    }

    /*
    *     private fun addFile(builder: MultipartBody.Builder, file: File, name: String, filename: String) {

        val encodedFileName = URLEncoder.encode(filename, "UTF-8")
        val headers = Headers.of("Content-Disposition", "file; filename=\"$encodedFileName\"", "Content-Transfer-Encoding", "binary")
        if (file.isDirectory) {
            // add directory
            builder.addPart(headers, RequestBody.create(MediaType.parse("application/x-directory"), ""))
            // add files and subdirectories
            for (f: File in file.listFiles()) {
                addFile(builder, f, f.name, filename + "/" + f.name)
            }
        } else {
            builder.addPart(headers, RequestBody.create(MediaType.parse("application/octet-stream"), file))
        }

    }*/

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