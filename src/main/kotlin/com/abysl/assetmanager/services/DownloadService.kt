package com.abysl.assetmanager.services

import androidx.compose.runtime.mutableStateOf
import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.db.tables.AssetIndexTable
import com.abysl.assetmanager.db.tables.AssetTable
import com.abysl.assetmanager.model.Asset
import com.abysl.assetmanager.model.AssetIndex
import com.abysl.assetmanager.util.parseFileNameFromUrl
import com.abysl.humble.model.HumbleProduct
import com.abysl.itch.Itch
import com.abysl.itch.ItchClientConfig
import com.abysl.itch.model.ItchGame
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import mu.KLogger
import net.lingala.zip4j.ZipFile
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class DownloadService(val maxJobs: Int = 5) : KoinComponent {
    val client by inject<HttpClient>()
    val logger by inject<KLogger>()


    private val jobs = MutableStateFlow<List<CoroutineScope>>(emptyList())
    private val downloadQueue = ConcurrentLinkedQueue<Asset>()
    private val managerScope = mutableStateOf(CoroutineScope(Dispatchers.IO))

    val filesLeft = flow {
        while (true) {
            emit(downloadQueue.size)
            delay(100)
        }
    }

    fun start() {
        managerScope.value.launch {
            while (true) {
                if (downloadQueue.isEmpty()) {
                    val assetsToDownload = getAssetsToDownload()
                    downloadQueue.addAll(assetsToDownload)
                    delay(1000)
                } else if (jobs.value.size < maxJobs) {
                    startJob()
                } else {
                    delay(100)
                }
            }
        }
    }

    fun stop() {
        managerScope.value.cancel("Service halted")
        managerScope.value = CoroutineScope(Dispatchers.IO)
        jobs.value.forEach { it.cancel("Download Service halted") }
        jobs.value = emptyList()
    }

    fun startJob() {
        val scope = CoroutineScope(Dispatchers.IO)
        val asset = downloadQueue.poll()
        scope.launch {
            downloadAsset(asset)
            jobs.value -= scope
        }
        jobs.value += scope
    }

    fun getAssetsToDownload(): List<Asset> = transaction {
        AssetTable.select { AssetTable.downloaded eq false }.map(AssetTable::fromRow)
    }

    suspend fun downloadAsset(asset: Asset) {
        delay(Random().nextLong(500, 1000))
        val downloadSuccess: Boolean = when (asset.sourceObject) {
            is ItchGame -> downloadItchAsset(asset.dir, asset.sourceObject)
            is HumbleProduct -> downloadHumbleAsset(asset.dir, asset.sourceObject)
            is File -> copyLocalAsset(asset.dir, asset.sourceObject)
            else -> true
        }
        indexAsset(asset)
        asset.markDownloaded(downloadSuccess)
    }

    fun indexAsset(asset: Asset){
        asset.dir.walkBottomUp().filter { it.isFile && it.extension == "zip" }.map { ZipFile(it) }.forEach {
            try {
                it.extractAll(asset.dir.path)
            } catch (e: Exception) {
                logger.warn("Failed to unzip ${it.file.path}")
            }
        }
        val files = asset.dir.walkBottomUp().filter { it.isFile }.map { AssetIndex(0, assetId = asset.id, file = it) }
        transaction {
            AssetIndexTable.batchInsert(files){
                this[AssetIndexTable.assetId] = it.assetId
                this[AssetIndexTable.filePath] = it.file.path
            }
        }
    }

    suspend fun downloadItchAsset(saveDir: File, game: ItchGame): Boolean {
        var success = true
        val iconPath = saveDir.resolve("icon.png")
        if (!iconPath.exists() && !downloadFile(iconPath, game.coverUrl, overwrite = true)) {
            success = false
        }
        val itch = Itch(ItchClientConfig(Prefs.itchApiKey))
        val downloads = itch.getDownloadUrls(game)
        for (download in downloads) {
            val downloadInfo = download.key
            val downloadUrl = download.value
            val saveFile = saveDir.resolve(downloadInfo.filename)
            if (saveFile.exists()) continue
            if (!downloadFile(saveFile, downloadUrl, overwrite = true)) success = false
        }
        return success
    }

    suspend fun downloadHumbleAsset(saveDir: File, product: HumbleProduct): Boolean {
        var success = true
        val iconPath = saveDir.resolve("icon.png")
        if (!iconPath.exists() && !downloadFile(iconPath, product.iconUrl, overwrite = true)) {
            success = false
        }
        for (download in product.downloads) {
            val saveFile = saveDir.resolve(parseFileNameFromUrl(download.web))
            if (saveFile.exists()) continue
            if (!downloadFile(saveFile, download.web, overwrite = true)) success = false
        }
        return success
    }

    fun copyLocalAsset(saveDir: File, assetSource: File): Boolean {
        return assetSource.copyRecursively(target = saveDir, overwrite = true)
    }

    suspend fun downloadFile(
        saveLocation: File,
        fileUrl: String,
        retries: Int = 2,
        delay: Long = Random().nextLong(2000, 5000),
        overwrite: Boolean = false
    ): Boolean {
        if (retries < 0) return false
        if (saveLocation.exists() && !overwrite) return true
        if (fileUrl == "null") return true

        saveLocation.delete()
        if (!saveLocation.createNewFile()) return false
        try {
            logger.info { "Attempting to download $fileUrl, $retries retries remaining" }
//            val response: HttpResponse = client.get(fileUrl)
            client.prepareGet(fileUrl).execute {
                val channel = it.bodyAsChannel()
                val byteBufferSize = 128000
                while (channel.availableForRead > 0 || !channel.isClosedForRead) {
                    if (channel.availableForRead < byteBufferSize) {
                        saveLocation.appendBytes(channel.readRemaining().readBytes())
                    } else {
                        saveLocation.appendBytes(channel.readPacket(byteBufferSize).readBytes())
                    }
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            delay(delay)
            return downloadFile(saveLocation, fileUrl, retries - 1)
        }
    }
}