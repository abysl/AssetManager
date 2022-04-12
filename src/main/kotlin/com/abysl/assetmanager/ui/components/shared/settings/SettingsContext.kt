package com.abysl.assetmanager.ui.components.shared.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.abysl.assetmanager.Prefs

class SettingsContext {
     var settings by mutableStateOf(
        Settings(
            Prefs.itchApiKey,
            Prefs.darkMode
        )
    )


    fun onSave() {
        Prefs.itchApiKey = settings.itchApiKey
        Prefs.darkMode = settings.darkMode
    }
}