package com.abysl.assetmanager.services

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.db.tables.ImageTable
import com.abysl.assetmanager.util.defaultImage
import com.abysl.assetmanager.util.loadLocalImage
import com.abysl.ipfs.network.endpoints.Endpoint
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import mu.KotlinLogging
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.util.*

class ImageService(val cacheLocation: File) {
    private val logger = KotlinLogging.logger {}
    val client = HttpClient(CIO)


    operator fun get(
        url: String, scope: CoroutineScope,
        delay: Long = 1000, maxAttempts: Int = 2
    ): MutableState<ImageBitmap> {
        val image = mutableStateOf(defaultImage)
        if (url.isEmpty()) return image
        CoroutineScope(Dispatchers.IO).launch {
            var attempt = 0
            while (attempt++ < maxAttempts) {
                val id = getImageId(url)
                if (id != null) {
                    withContext(scope.coroutineContext) {
                        image.value = loadLocalImage(Prefs.IMAGE_CACHE.resolve("$id.png"))
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
            try {
                val response: HttpResponse = client.get(url)
                cacheLocation.resolve("$id.png").also { it.createNewFile() }.writeBytes(response.readBytes())
            } catch (e: Exception) {
                logger.warn("Failed to load image $url")
            }
        }
    }


    private fun getImageId(url: String): Int? {
        val result = transaction {
            ImageTable.select { ImageTable.url eq url }.firstOrNull()
        } ?: return null
        return ImageTable.fromRow(result).id
    }
}