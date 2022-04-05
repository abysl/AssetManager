package com.abysl.humble.model

@kotlinx.serialization.Serializable
data class DownloadData(
    val platform: String,
    val downloads: List<DownloadStruct>
)