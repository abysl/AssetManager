package com.abysl.ipfs.model

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class IpfsHash (
    @SerialName("/")
    val hash: String
)