package com.abysl.assetmanager.ui.components.assetimport

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.services.AssetImportService
import com.abysl.assetmanager.services.DownloadService
import com.abysl.humble.Humble
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AssetImportContext: KoinComponent {
    var selectedImportType by mutableStateOf(SourcePlatform.values().first())
    val importService by inject<AssetImportService>()

    fun itchImport(){
        try{
            importService.importItchAssets()
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }

    fun humbleImport(humbleJson: String){
        try{
            val products = Humble().deserializeJson(Prefs.jsonFormat.decodeFromString(humbleJson))
            importService.importHumbleAssets(products)
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }
}