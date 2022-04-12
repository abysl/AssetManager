package com.abysl.assetmanager.ui.components.assetimport

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AssetImportContext {
    var selectedImportType by mutableStateOf(ImportTypes.LOCAL)
}