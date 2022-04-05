package com.abysl.ipfs.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class IpfsFileType(val value: String) {
    DIRECTORY("1"),
    FILE("2")
}