package com.abysl.ipfs.filesystems

import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toPath
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class IpfsAssetManagerTest {

    val assets = IpfsAssetManager(cachePath = "build/test", rootHash = "QmYUpiwrJCajh5jrcnWnUHDAR5A4gZ74qFzRiwSBdW52aQ")

    @Test
    fun get() {

    }

    @Test
    fun cache() {
        runBlocking {
            val folder1 = "folder1"
            val asset1 = "asset1.txt"
            val asset2 = "asset2.txt"
            val asset3 = "folder1/asset3.txt"

            assets.cache(asset1)
            assert(assets.getFromCache(asset1).readText() == "asset1")
            assets.cache(folder1, true)
            assert(assets.getFromCache(folder1).isDirectory)
            assert(assets.getFromCache(asset3).readText() == "asset3")
            try {
                // Validate that asset2 is not found in the cache
                assets.getFromCache(asset2)
                assert(false)
            } catch (e: Exception){

            }
        }
    }

    @Test
    fun isDirectory() {
        val isDir = runBlocking {  assets.isDirectory("folder1") }
        assert(isDir)
        val isDir2 = runBlocking { assets.isDirectory("asset1.txt") }
        assert(!isDir2)
        val isDir3 = runBlocking { assets.isDirectory("folder1/asset3.txt") }
        assert(!isDir3)
        val isDir4 = runBlocking { assets.isDirectory("/") }
        assert(isDir4)
    }
}