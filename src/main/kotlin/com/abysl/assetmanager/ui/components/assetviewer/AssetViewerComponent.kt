package com.abysl.assetmanager.ui.components.assetviewer

import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.model.Asset
import com.abysl.assetmanager.services.ImageService
import com.abysl.assetmanager.ui.components.Component
import com.abysl.assetmanager.ui.util.Theme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AssetViewerComponent(val context: AssetViewerContext = AssetViewerContext()) : Component(), KoinComponent {
    val images by inject<ImageService>()

    val assets = context.getAssets()

    @Composable
    override fun view() {
        assetList(assets)
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
    fun assetCard(asset: Asset) {
        Row(modifier = Modifier.padding(10.dp)
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