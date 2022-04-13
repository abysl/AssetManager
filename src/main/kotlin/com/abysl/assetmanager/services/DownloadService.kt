package com.abysl.assetmanager.services

import ch.qos.logback.core.util.FileUtil
import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.db.tables.AssetTable
import com.abysl.assetmanager.model.Asset
import com.abysl.assetmanager.ui.components.assetimport.SourcePlatform
import com.abysl.assetmanager.util.downloadFile
import com.abysl.humble.model.HumbleProduct
import com.abysl.itch.Itch
import com.abysl.itch.ItchClientConfig
import com.abysl.itch.model.ItchGame
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.io.File
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

class DownloadService(val maxJobs: Int = 20) {


    private val jobs = mutableListOf<CoroutineScope>()
    private val downloadQueue = ConcurrentLinkedQueue<Asset>()
    private var running = MutableStateFlow(false)

    val filesLeft = flow {
        while (true) {
            emit(downloadQueue.size)
            delay(100)
        }
    }

    fun start() {
        running.value = true
        CoroutineScope(Dispatchers.IO).launch {
            while (running.value) {
                if (downloadQueue.isNotEmpty() && jobs.size < maxJobs) {
                    val scope = CoroutineScope(Dispatchers.IO)
                    scope.launch {
                        downloadAsset(downloadQueue.poll())
                    }
                    jobs.add(scope)
                } else {
                    if(downloadQueue.isEmpty()) {
                        val assetsToDownload = transaction {
                            AssetTable.select { AssetTable.downloaded eq false }.map(AssetTable::fromRow)
                        }
                        downloadQueue.addAll(assetsToDownload)
                    }
                    delay(10000)
                }
            }
        }
    }

    fun stop() {
        running.value = false
        jobs.forEach { it.cancel("Download Service halted") }
    }

    suspend fun downloadAsset(asset: Asset) {
        val assetDir = Prefs.ASSET_PATH.resolve("${asset.id}").also { it.mkdirs() }
        val success = when (asset.sourcePlatform) {
            SourcePlatform.ITCH -> downloadItchAsset(assetDir, asset.sourceObject as ItchGame)
            SourcePlatform.HUMBLE -> downloadHumbleAsset(assetDir, asset.sourceObject as HumbleProduct)
            else -> false
        }
        transaction { AssetTable.update({ AssetTable.id eq asset.id }) { it[downloaded] = success } }
    }

    suspend fun downloadItchAsset(assetDir: File, game: ItchGame): Boolean {
        downloadFile(assetDir.resolve("icon.png"), game.coverUrl, overwrite = true)
        val itch = Itch(ItchClientConfig(Prefs.itchApiKey))
        for (download in itch.getDownloadUrls(game)) {
            val downloadInfo = download.key
            val downloadUrl = download.value
            val successful = downloadFile(assetDir.resolve(downloadInfo.filename), downloadUrl, overwrite = true)
            if (!successful) return false
        }
        return true
    }

    suspend fun downloadHumbleAsset(assetDir: File, product: HumbleProduct): Boolean {
        downloadFile(assetDir.resolve("icon.png"), product.iconUrl)
        for (download in product.downloads) {
            val successful = downloadFile(assetDir.resolve(download.name), download.web)
            if (!successful) return false
        }
        return true
    }
}