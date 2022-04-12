package com.abysl.assetmanager.ui.components.shared.settings

import androidx.compose.runtime.mutableStateOf
import com.abysl.assetmanager.Prefs

class SettingsContext {
    private val settings_ = mutableStateOf(
        Settings(
            Prefs.itchApiKey,
        )
    )
    var settings: Settings
        get() = settings_.value
        set(value) {
            settings_.value = value
        }


    fun onSave() {
        Prefs.itchApiKey = settings.itchApiKey
    }
}