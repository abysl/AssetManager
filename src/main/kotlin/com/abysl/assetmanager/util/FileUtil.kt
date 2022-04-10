package com.abysl.assetmanager.util

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import java.io.File
import java.io.InputStream


fun String.asResourceStream(): InputStream =
    object {}.javaClass.classLoader.getResourceAsStream(this) ?: throw ResourceLoadException(this)

class ResourceLoadException(resource: String): Exception("Failed to load resource: $resource")



