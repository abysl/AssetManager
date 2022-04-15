package com.abysl.assetmanager.ui.components.assetviewer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.abysl.assetmanager.db.tables.AssetIndexTable
import com.abysl.assetmanager.db.tables.AssetTable
import com.abysl.assetmanager.model.Asset
import com.abysl.assetmanager.model.AssetIndex
import com.abysl.assetmanager.model.AssetIndexScore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class AssetViewerContext {

    var assetIndex by mutableStateOf(emptyList<AssetIndex>())
    var searchText by mutableStateOf("")
    var results by mutableStateOf(listOf<AssetIndexScore>())
    val enableExtensionFilter = MutableStateFlow(false)
    val extensionFilter = MutableStateFlow(listOf("png", "jpg"))

    var updateScope = CoroutineScope(Dispatchers.Default)

    init {
        CoroutineScope(Dispatchers.IO).launch { assetIndex = getIndex() }
        updateIndexScores()
    }

    fun updateIndexScores() {
        results = emptyList()
        updateScope.cancel()
        updateScope = CoroutineScope(Dispatchers.Default)
        updateScope.launch {
            results = chunkedRank(searchText).sorted()
        }
    }

    fun getAssets(): List<Asset> {
        val assets = transaction { AssetTable.selectAll().map(AssetTable::fromRow) }
        return assets
    }

    suspend fun chunkedRank(matchString: String): List<AssetIndexScore> {
        val index = filterIndex(assetIndex)
        if(index.isEmpty()) return emptyList()
        return index.chunked(index.size / 32).map { assets ->
            assets.map { asset ->
                withContext(Dispatchers.Default) {
                    rankMatch(matchString, asset)
                }
            }
        }.flatten()
    }

    fun rankMatch(matchString: String, assetIndex: AssetIndex): AssetIndexScore {
        val score = FuzzySearch.ratio(matchString, assetIndex.file.name)
        return AssetIndexScore(score, assetIndex)
    }

    suspend fun filterIndex(index: List<AssetIndex>): List<AssetIndex> = coroutineScope {
        index.filter {
            !enableExtensionFilter.value || it.file.extension in extensionFilter.value
        }
    }

    fun indexChunkFlow(chunkSize: Int = 32): Flow<List<AssetIndex>> = flow {
        var chunk = 0
        val size = transaction { AssetIndexTable.selectAll().count() }
        do {
            emit(transaction {
                AssetIndexTable.select {
                    AssetIndexTable.id inList ((chunk * chunkSize) + 1..chunk * chunkSize + chunkSize)
                }.map(AssetIndexTable::fromRow)
            })
        } while (chunk++ < (size - 1) / chunkSize)
    }

    fun getIndex(): List<AssetIndex> = transaction {
        AssetIndexTable.selectAll().map(AssetIndexTable::fromRow).filter { it.file.isFile }
    }

    fun parseExtensions(extensions: String) {
        val parsedExtensions = extensions.replace(" ", "").replace(".", "").split(",")
        extensionFilter.value = parsedExtensions
    }


}
