package com.abysl.assetmanager.ui.components.assetimport

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.ui.components.Component
import com.abysl.assetmanager.ui.components.shared.dropdown.DropDownComponent
import com.abysl.assetmanager.ui.components.shared.util.AbyslComponents.LinkText
import org.koin.core.component.KoinComponent

class AssetImportComponent(val ctx: AssetImportContext = AssetImportContext()) : Component(), KoinComponent {

    @Composable
    override fun view() {
        Column(verticalArrangement = Arrangement.spacedBy(15.dp), modifier = Modifier.padding(15.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(15.dp), verticalAlignment = Alignment.CenterVertically) {
                importSelector()
                when (ctx.selectedImportType) {
                    SourcePlatform.HUMBLE -> LinkText(
                        "Humble Json Data - Click Here For Tutorial",
                        "https://youtu.be/BojZ0AIBfc8"
                    )
                    SourcePlatform.ITCH -> LinkText(
                        "Itch Api Key - Click here to get one",
                        "https://itch.io/user/settings/api-keys"
                    )
                    else -> {}
                }
            }
            when (ctx.selectedImportType) {
                SourcePlatform.ITCH -> itchImportForm()
                SourcePlatform.HUMBLE -> humbleImportForm()
                SourcePlatform.LOCAL -> localImportForm()
            }
        }
    }

    @Composable
    fun importSelector() {
        DropDownComponent(
            items = SourcePlatform.values().asIterable(),
            onItemSelect = { ctx.selectedImportType = it },
            fieldSize = FIELD_SIZE
        ).view()
    }

    @Composable
    fun itchImportForm() {
        var apiKey by remember { mutableStateOf(Prefs.itchApiKey) }
        TextField(apiKey, onValueChange = { apiKey = it })
        importButton {
            Prefs.itchApiKey = apiKey
            ctx.itchImport()
        }
    }

    @Composable
    fun humbleImportForm() {
        var importJson: String by remember { mutableStateOf("") }
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            TextField(
                value = importJson, onValueChange = { importJson = it },
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    ctx.humbleImport(importJson)
                }) {
                    Text("Import All")
                }
            }
        }
    }

    @Composable
    fun localImportForm() {
        Text("Not implemented yet")
    }

    @Composable
    fun importButton(onClick: () -> Unit) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Button(onClick = onClick) {
                Text("Import All")
            }
        }
    }

    companion object {
        private val FIELD_SIZE = 200.dp;
        private val BORDER_SIZE = 2.dp
    }
}