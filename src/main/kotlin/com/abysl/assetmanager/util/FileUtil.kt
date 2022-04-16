package com.abysl.assetmanager.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image
import java.io.File
import java.io.InputStream
import java.util.*


fun String.asResourceStream(): InputStream =
    object {}.javaClass.classLoader.getResourceAsStream(this) ?: throw ResourceLoadException(this)

class ResourceLoadException(resource: String) : Exception("Failed to load resource: $resource")


val fileNameRegex = """(?:.+\/)([^#?&]+)""".toRegex()
fun parseFileNameFromUrl(url: String): String {
    val test = fileNameRegex.find(url)?.groupValues?.last() ?: "unknown_filename"
    return test
}






