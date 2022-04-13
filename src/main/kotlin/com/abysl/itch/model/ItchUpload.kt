package com.abysl.itch.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ItchUpload(
    val filename: String,
    val size: ULong,
    val id: ULong,
    val type: String,
    @SerialName("game_id") val gameId: ULong,
    @SerialName("md5_hash") val md5: String? = null,
)