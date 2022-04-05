package com.abysl.ipfs.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class IpfsMessage(
    @SerialName("Message") val message: String,
    @SerialName("Code") val code: String,
    @SerialName("Type") val type: String,
)