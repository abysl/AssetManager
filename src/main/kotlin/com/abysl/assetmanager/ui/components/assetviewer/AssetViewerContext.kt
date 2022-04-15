package com.abysl.assetmanager.ui.components.assetviewer

import androidx.compose.runtime.mutableStateOf
import com.abysl.assetmanager.db.tables.AssetIndexTable
import com.abysl.assetmanager.db.tables.AssetTable
import com.abysl.assetmanager.model.Asset
import com.abysl.assetmanager.model.AssetIndex
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.descriptors.serialDescriptor
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class AssetViewerContext {

    val searchText = MutableStateFlow("")
    val enableExtensionFilter = MutableStateFlow(false)
    val extensionFilter = MutableStateFlow(listOf("png", "jpg"))

    val fuzzyResultState = mutableStateOf(sortedMapOf<Int, AssetIndex>())
    val assetIndex = getIndex()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            searchText.collectLatest { searchString ->
                fuzzyResultState.value = sortedMapOf(compareByDescending { it })
                chunkedRank(searchString, assetIndex, 10)
            }
        }
    }

    fun getAssets(): List<Asset> {
        val assets = transaction { AssetTable.selectAll().map(AssetTable::fromRow) }
        return assets
    }

    suspend fun chunkedRank(matchString: String, assets: List<AssetIndex>, top: Int) = coroutineScope {
        assets
            .chunked(32)
            .forEach { assets ->
                withContext(Dispatchers.IO) {
                    assets.forEach {
                        val score = rankMatch(matchString, it)
                        if (fuzzyResultState.value.isEmpty() || score > fuzzyResultState.value.firstKey())
                            fuzzyResultState.value += score to it
                        if (fuzzyResultState.value.size > top)
                            fuzzyResultState.value -= fuzzyResultState.value.firstKey()
                    }
                }
            }
    }

    fun rankMatch(matchString: String, assetIndex: AssetIndex): Int {
        return FuzzySearch.partialRatio(matchString, assetIndex.file.path)
    }

    fun getIndex(): List<AssetIndex> = transaction {
        AssetIndexTable.selectAll().map(AssetIndexTable::fromRow)
    }.filter {
        it.file.isFile && (!enableExtensionFilter.value || it.file.extension in extensionFilter.value)
    }

    fun parseExtensions(extensions: String) {
        val parsedExtensions = extensions.replace(" ", "").replace(".", "").split(",")
        extensionFilter.value = parsedExtensions
    }


}
