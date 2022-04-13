package com.abysl.humble.model

@kotlinx.serialization.Serializable
data class HumbleProduct(
    val name: String,
    val url: String,
    val iconUrl: String,
    val creator: String,
    val downloads: List<DownloadStruct>,
)