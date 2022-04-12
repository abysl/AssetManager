package com.abysl.itch.model

@kotlinx.serialization.Serializable
data class ItchGameKey(
    val gameId: ULong,
    val gameKey: String,
)
