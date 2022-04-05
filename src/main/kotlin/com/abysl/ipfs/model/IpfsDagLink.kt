package com.abysl.ipfs.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class IpfsDagLink(
    @SerialName("Hash")
    val hash: IpfsHash,
    @SerialName("Name")
    val name: String,
    @SerialName("Tsize")
    val size: ULong
)