package com.abysl.ipfs.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class IpfsLink(
    @SerialName("Name") val name: String,
    @SerialName("Hash") val hash: String,
    @SerialName("Size") val size: ULong,
    @SerialName("Type") val type: String,
    @SerialName("Target") val target: String,
) {
}