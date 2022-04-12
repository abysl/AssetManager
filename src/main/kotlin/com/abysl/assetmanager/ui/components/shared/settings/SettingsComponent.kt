package com.abysl.assetmanager.ui.components.shared.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.services.NavigationService
import com.abysl.assetmanager.ui.components.Component
import com.abysl.assetmanager.ui.components.shared.util.AbyslComponents.LinkText
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsComponent(val context: SettingsContext = SettingsContext()) : Component(), KoinComponent {
    val nav by inject<NavigationService>()

    @Composable
    override fun view() {

        var darkmode by remember { mutableStateOf(Prefs.darkMode) }

        Column(
            modifier = Modifier.padding(15.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            LinkText(
                "Itch Api Key - Click here to get one",
                "https://itch.io/user/settings/api-keys"
            )
            TextField(
                value = context.settings.itchApiKey,
                onValueChange = {
                    context.settings = context.settings.copy(itchApiKey = it)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = darkmode,
                    onCheckedChange = {
                        context.settings = context.settings.copy(darkMode = it)
                        darkmode = it
                    })
                Text("Dark Mode (requires restart)")
            }
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.fillMaxSize()
            ) {
                Button(onClick = {
                    context.onSave()
                    nav.back()
                }) { Text("Save") }
            }
        }
    }
}