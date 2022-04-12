package com.abysl.assetmanager.ui.components.shared.settings

@kotlinx.serialization.Serializable
data class Settings(
    val itchApiKey: String,
    val darkMode: Boolean
)