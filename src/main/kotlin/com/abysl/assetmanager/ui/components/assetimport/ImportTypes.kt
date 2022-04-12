package com.abysl.assetmanager.ui.components.assetimport

enum class ImportTypes(val humanReadable: String) {
    ITCH("Itch.io"),
    HUMBLE("Humble Bundle"),
    LOCAL("Local");

    override fun toString(): String = humanReadable
}