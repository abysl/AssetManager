package com.abysl.assetmanager.model

import java.io.File

data class AssetIndex(
    val id: Int,
    val assetId: Int,
    val file: File,
)