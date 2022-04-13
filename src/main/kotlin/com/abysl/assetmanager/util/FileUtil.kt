package com.abysl.assetmanager.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.abysl.assetmanager.Prefs
import com.abysl.ipfs.network.endpoints.Endpoint
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jetbrains.skia.Image
import java.io.File
import java.io.InputStream
import java.util.*


fun String.asResourceStream(): InputStream =
    object {}.javaClass.classLoader.getResourceAsStream(this) ?: throw ResourceLoadException(this)

class ResourceLoadException(resource: String) : Exception("Failed to load resource: $resource")

fun loadLocalImage(file: File): ImageBitmap {
    val imageBytes = if (file.exists())
        file.readBytes()
    else
        return defaultImage
    return Image.makeFromEncoded(imageBytes).toComposeImageBitmap()
}

val defaultImage by lazy {
    Image.makeFromEncoded("default.png".asResourceStream().readBytes()).toComposeImageBitmap()
}

suspend fun downloadFile(
    saveLocation: File,
    fileUrl: String,
    retries: Int = 2,
    delay: Long = Random().nextLong(2000, 5000),
    overwrite: Boolean = false
): Boolean {
    if (retries < 0) return false
    if (saveLocation.exists() && !overwrite) return false
    try {
        println("Attempting to download $fileUrl")
        val response: HttpResponse = HttpClient(CIO).get(fileUrl)
        saveLocation.also { it.createNewFile() }.writeBytes(response.readBytes())
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return downloadFile(saveLocation, fileUrl, retries - 1)
    }
}




