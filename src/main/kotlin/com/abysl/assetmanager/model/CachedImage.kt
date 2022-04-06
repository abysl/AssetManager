package com.abysl.assetmanager.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.util.asResourceStream
import okio.Path.Companion.toPath
import org.jetbrains.skia.Image

data class CachedImage (
    val id: Int,
    val url: String,
    )