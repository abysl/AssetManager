package com.abysl.assetmanager.ui.components.assetviewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.model.Asset
import com.abysl.assetmanager.model.AssetIndexScore
import com.abysl.assetmanager.services.ImageService
import com.abysl.assetmanager.ui.components.Component
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AssetViewerComponent(val context: AssetViewerContext = AssetViewerContext()) : Component() {

    val assets = context.getAssets()

    @Preview
    @Composable
    override fun view() {
        Column() {
            searchBar()
            if (context.searchText.isBlank()) {
                assetList(assets)
            } else {
                fileList(context.results)
            }
        }
    }

    @Preview
    @Composable
    fun searchBar() {
        var showFilterMenu by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.fillMaxWidth().padding(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            TextField(
                context.searchText,
                onValueChange = {
                    context.searchText = it
                    context.updateIndexScores()
                },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { showFilterMenu = true },
                enabled = !showFilterMenu
            ) {
                Icon(
                    Icons.Filled.FilterList,
                    contentDescription = "Filter"
                )
                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false },
                    offset = DpOffset(0.dp, 30.dp),
                    modifier = Modifier
                        .width(300.dp)
                        .border(2.dp, MaterialTheme.colors.primary)
                        .background(MaterialTheme.colors.surface)
                        .padding(15.dp)
                ) {
                    filterMenu()
                }
            }

        }
    }

    @Composable
    fun filterMenu() {
        Text("Filter by...")
        Row() {
            Checkbox(
                context.enableExtensionFilter.collectAsState().value,
                onCheckedChange = { context.enableExtensionFilter.value = it })
            TextField(
                value = context.extensionFilter.collectAsState().value.joinToString(", "),
                onValueChange = { context.parseExtensions(it) }
            )
        }
    }

    @Composable
    fun assetList(assets: List<Asset>) {
        Box() {
            val state = rememberLazyListState()
            LazyColumn(
                state = state,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                for (asset in assets) {
                    item {
                        assetCard(asset)
                    }
                }

            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                )
            )
        }
    }

    @Composable
    fun fileList(assetIndex: List<AssetIndexScore>) {
        Box() {
            val state = rememberLazyListState()
            LazyColumn(
                state = state,
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                for (indice in assetIndex) {
                    item {
                        card {
                            assetCard(indice.assetIndex.asset)
                            fileCard(indice)
                        }
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                )
            )
        }
    }

    @Composable
    fun card(content: @Composable () -> Unit) {
        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            content()
        }
    }


    @Composable
    fun assetCard(asset: Asset) {
        card {
            context.imageService.image(asset.icon, asset.name)
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                Text(asset.name)
                Text(asset.creator)
            }
        }
    }

    @Composable
    fun fileCard(indice: AssetIndexScore) {
        card {
            when (indice.assetIndex.file.extension) {
                in listOf("png", "jpg") ->
                    context.imageService.image(
                        indice.assetIndex.file,
                        indice.assetIndex.file.name
                    )
                in listOf("wav", "ogg", "mp3") -> {
                    IconButton(onClick = { context.audioService.toggleAudio(indice.assetIndex.file) }) {
                        Icon(
                            Icons.Filled.GraphicEq,
                            contentDescription = "Menu"
                        )
                    }
                }
                else -> {}
            }
            Text("${indice.assetIndex.file.path} ${indice.score}% match")
        }
    }

}