package com.abysl.itch.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ItchGame(
    val id: ULong,
    val url: String,
    val title: String,
    val classification: String,
    @SerialName("user") val creator: ItchUser,
    @SerialName("cover_url") val coverUrl: String,
    @SerialName("short_text") val description: String,
)