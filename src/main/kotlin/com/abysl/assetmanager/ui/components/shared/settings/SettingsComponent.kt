package com.abysl.assetmanager.ui.components.shared.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.ui.components.Component

class SettingsComponent(val context: SettingsContext = SettingsContext()) : Component() {
    @Composable
    override fun view() {
        var editable by remember { mutableStateOf(true) }
        AnimatedVisibility(visible = editable) {

            Column(modifier = Modifier.padding(10.dp)) {
                TextField(
                    value = context.settings.itchApiKey,
                    onValueChange = { context.settings = context.settings.copy(itchApiKey = it) },
                )
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = { context.onSave() }) { Text("Save") }
                }
            }
        }
    }
}