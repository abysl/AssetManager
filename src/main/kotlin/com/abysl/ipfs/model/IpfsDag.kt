package com.abysl.ipfs.model

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class IpfsDag (
    @SerialName("Links")
    val links: List<IpfsDagLink> = listOf(),
)