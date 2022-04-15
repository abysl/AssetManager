package com.abysl.assetmanager.ui.components.assetviewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.model.Asset
import com.abysl.assetmanager.model.AssetIndex
import com.abysl.assetmanager.ui.components.Component
import org.koin.core.component.KoinComponent

class AssetViewerComponent(val context: AssetViewerContext = AssetViewerContext()) : Component(), KoinComponent {

    val assets = context.getAssets()

    @Preview
    @Composable
    override fun view() {
        Column() {
            searchBar()
            if (context.searchText.collectAsState().value.isBlank()) {
                assetList(assets)
            } else {
                fileList(context.fuzzyResultState.value)
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
                context.searchText.collectAsState().value,
                onValueChange = { context.searchText.value = it },
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
    fun fileList(assetIndex: Map<Int, AssetIndex>) {
        Box() {
            val state = rememberLazyListState()
            LazyColumn(
                state = state,
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                for (indice in assetIndex) {
                    item {
                        Text("${indice.key} ${indice.value.file.path}")
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
    fun assetCard(asset: Asset) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Image(
                painter = BitmapPainter(asset.getIcon()),
                contentDescription = asset.name,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize(0.1f),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(asset.name)
                Text(asset.creator)
            }
        }
    }
}