package com.abysl.assetmanager.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.abysl.assetmanager.util.asResourceStream
import okio.Path.Companion.toPath
import org.jetbrains.skia.Image.Companion.makeFromEncoded


data class GameCard(
    val name: String,
    val scryfall_id: String,
    val image_path: String,
) {

    fun loadImage(): ImageBitmap {
        val imageFile = image_path.toPath().toFile()
        val imageBytes =
            if(imageFile.exists())
                imageFile.readBytes()
        else
            "images/default-image.png".asResourceStream().readBytes()
        return makeFromEncoded(imageBytes).toComposeImageBitmap()
    }
}