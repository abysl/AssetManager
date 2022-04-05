package com.abysl.humble.model

@kotlinx.serialization.Serializable
data class SubProduct(
    val name: String,
    val url: String,
    val icon: String,
    val downloads: List<DownloadData>,
) { }