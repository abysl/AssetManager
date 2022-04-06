package com.abysl.humble.model

@kotlinx.serialization.Serializable
data class SubProduct(
    val name: String,
    val url: String,
    val iconUrl: String,
    val creator: String,
    val downloads: HashMap<String, List<DownloadStruct>>,
)