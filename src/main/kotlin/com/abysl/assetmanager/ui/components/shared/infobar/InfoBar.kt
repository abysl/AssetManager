package com.abysl.assetmanager.ui.components.shared.infobar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.ui.components.Component
import com.abysl.assetmanager.ui.util.Theme

class InfoBar(val ctx: InfoBarContext = InfoBarContext()) : Component() {

    @Composable
    override fun view() {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().background(color = Theme.lightGray)
        ) {
            downloadStatus()
        }
    }

    @Composable
    fun downloadStatus() {
        val filesLeft by ctx.downloadService.filesLeft.collectAsState(0)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(15.dp).fillMaxWidth()
        ) {
            if (filesLeft == 0) {
                Text("All assets downloaded")
            } else if (ctx.downloading.value) {
                Text("Downloading... $filesLeft assets remaining")
            } else {
                Text("Paused... $filesLeft assets remaining")
            }
            IconButton(onClick = ctx::onToggleDownload) {
                if (ctx.downloading.value) {
                    Icon(
                        Icons.Filled.Pause,
                        contentDescription = "Pause"
                    )
                } else {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = "Play"
                    )
                }
            }
        }
    }
}