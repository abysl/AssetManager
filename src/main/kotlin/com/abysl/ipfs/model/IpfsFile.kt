package com.abysl.ipfs.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IpfsFile(
    @SerialName("Hash") val hash: String,
    @SerialName("Size") val size: ULong,
    @SerialName("CumulativeSize") val cumululativeSize: ULong,
    @SerialName("Blocks") val blocks: Int,
    @SerialName("Type") val type: String,
) { }