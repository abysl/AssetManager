package com.abysl.assetmanager.ui.components.shared.infobar

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.abysl.assetmanager.services.DownloadService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InfoBarContext(): KoinComponent {
    val downloadService by inject<DownloadService>()
    val downloading = mutableStateOf(true)

    fun onToggleDownload(){
        downloading.value = !downloading.value
        if(downloading.value){
            downloadService.start()
        }else {
            downloadService.stop()
        }
    }
}