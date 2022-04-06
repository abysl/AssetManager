package com.abysl.assetmanager.services

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.abysl.assetmanager.db.tables.ImageTable
import com.abysl.assetmanager.util.asResourceStream
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.skia.Image
import java.io.File

class ImageService(val cacheLocation: File) {
    val client = HttpClient(CIO)

    val defaultImage by lazy {
        Image.makeFromEncoded("default.png".asResourceStream().readBytes()).toComposeImageBitmap()
    }

    operator fun get(
        url: String, scope: CoroutineScope,
        delay: Long = 1000, maxAttempts: Int = 2
    ): MutableState<ImageBitmap> {
        val image = mutableStateOf(defaultImage)
        CoroutineScope(Dispatchers.IO).launch {
            var attempt = 0
            while (attempt++ < maxAttempts) {
                val id = getImageId(url)
                if (id != null) {
                    withContext(scope.coroutineContext) {
                        image.value = loadLocalImage("$id.png")
                    }
                } else {
                    cache(url)
                    delay(delay)
                }
            }
        }
        return image
    }

    fun cache(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val id = transaction { ImageTable.insertAndGetId { it[ImageTable.url] = url } }
            val response: HttpResponse = client.get(url)
            cacheLocation.resolve("$id.png").also { it.createNewFile() }.writeBytes(response.readBytes())
        }
    }

    fun loadLocalImage(path: String): ImageBitmap {
        val imageFile = cacheLocation.resolve(path)
        val imageBytes = if (imageFile.exists()) imageFile.readBytes()
        else return defaultImage
        return Image.makeFromEncoded(imageBytes).toComposeImageBitmap()
    }

    private fun getImageId(url: String): Int? {
        val result = transaction {
            ImageTable.select { ImageTable.url eq url }.firstOrNull()
        } ?: return null
        return ImageTable.fromRow(result).id
    }
}