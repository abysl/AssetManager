package com.abysl.ipfs.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class IpfsLs(
    @SerialName("Objects") val objects: List<IpfsFolderChildren>
)

@kotlinx.serialization.Serializable
data class IpfsFolderChildren(
    @SerialName("Hash") val hash: String,
    @SerialName("Links") val links: List<IpfsLink>
)