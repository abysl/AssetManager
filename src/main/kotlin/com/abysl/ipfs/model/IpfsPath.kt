package com.abysl.ipfs.model

data class IpfsPath(val path: String){

    override fun toString(): String {
        var formatted = path.replace(" ", "%20")
        if(!path.startsWith("/")) formatted = "/$formatted"
        return formatted
    }
}
