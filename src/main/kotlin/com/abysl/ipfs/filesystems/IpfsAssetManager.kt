package com.abysl.ipfs.filesystems

import com.abysl.ipfs.network.endpoints.Ipfs
import com.abysl.ipfs.model.IpfsFileType
import kotlinx.coroutines.*
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

class IpfsAssetManager(cachePath: String, rootHash: String, val ipfs: Ipfs = Ipfs()) {

    private val cacheFolder = cachePath.toPath().toFile().also { it.mkdirs() }
    private val rootHash = rootHash.replace("/", "").toPath()

    operator fun get(asset: String): File {
        runBlocking { cache(asset, true) }
        return getFromCache(asset)
    }

    fun getFromCache(asset: String): File {
        val path = asset.toPath()
        val file = "${cacheFolder.path}/$path".toPath().toFile()
        if (file.exists()) {
            return file
        } else {
            throw Exception("Was not able to find $path in the asset cache")
        }
    }

    suspend fun cache(asset: String, recursive: Boolean = false) {
        val assetPath = asset.toPath()
        val ipfsPath = rootHash.resolve(assetPath).toString().replace("\\", "/")
        if (!isDirectory(asset)) {
            val downloadFile = cacheFolder.resolve(assetPath.toString())
            ipfs.downloadFile(downloadLocation = downloadFile, ipfsPath)
        } else if (recursive) {
            ipfs.ls(ipfsPath).objects.firstOrNull()?.links?.forEach {
                cache(assetPath.resolve(it.name).toString(), recursive)
            }
        }
    }

    fun assetToIpfsPath(asset: Path): String {
        return "$rootHash/$asset"
    }

    suspend fun isDirectory(asset: String): Boolean {
        val assetPath = asset.toPath()
        if (assetPath.isRoot) return true
        val parent = assetPath.parent.toString()
        ipfs.ls("$rootHash/$parent").objects.firstOrNull()?.links?.forEach {
            if (it.name == assetPath.name && it.type == IpfsFileType.DIRECTORY.value) {
                return true
            }
        }
        return false
    }
}