package com.abysl.assetmanager.util

import java.io.InputStream


fun String.asResourceStream(): InputStream =
    object {}.javaClass.classLoader.getResourceAsStream(this) ?: throw ResourceLoadException(this)

class ResourceLoadException(resource: String): Exception("Failed to load resource: $resource")

