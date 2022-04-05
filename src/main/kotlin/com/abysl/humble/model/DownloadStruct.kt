package com.abysl.humble.model

@kotlinx.serialization.Serializable
data class DownloadStruct(
    val name: String,
    val web: String,
    val torrent: String,
    val humanSize: String,
    val fileSize: Long,
    val sha1: String,
    val md5: String,
)