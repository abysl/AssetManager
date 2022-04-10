package com.abysl.itch.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ItchUser(
    val username: String,
    val gamer: Boolean? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("cover_url") val coverUrl: String? = null,
    @SerialName("url") val url: String,
    @SerialName("press_user") val pressUser: Boolean? = null,
    val developer: Boolean? = null,
    val id: ULong,
)