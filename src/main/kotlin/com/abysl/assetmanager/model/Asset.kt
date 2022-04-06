package com.abysl.assetmanager.model

import androidx.compose.ui.graphics.ImageBitmap
import com.abysl.humble.model.DownloadStruct

@kotlinx.serialization.Serializable
data class Asset (
    val name: String,
    val creator: String,
    val iconUrl: String,
    val sourceUrl: String,
    val downloads: HashMap<String, List<DownloadStruct>>
)
