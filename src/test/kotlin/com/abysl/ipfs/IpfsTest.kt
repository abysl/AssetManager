package com.abysl.ipfs

import com.abysl.ipfs.network.endpoints.Ipfs
import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toPath
import org.junit.jupiter.api.Test

internal class IpfsTest {

    val ipfs = Ipfs()

    @Test
    fun statFile() {
        runBlocking {
            val file = ipfs.files.stat("/")
            assert(file.type == "directory")
        }
    }

    @Test
    fun readFile() {
        runBlocking {
            val text: String? = ipfs.files.read("test.txt.txt").readUTF8Line(20)
            assert(text == "hailo world")
        }
    }

    @Test
    fun getFile(){
        runBlocking {
            val fileStore = "build/test".toPath().toFile().also { it.mkdirs() }
            val file = fileStore.resolve("get_test.zip")
            val bytes = ipfs.get("QmfUFCfDSUXnXpqWJc7UXkG9C5x4PfuSGD7LkPLfEutcL6")
            file.writeBytes(bytes)
        }
    }

    @Test
    fun cat(){
        runBlocking {
            val fileStore = "build/test".toPath().toFile().also { it.mkdirs() }
            val file = fileStore.resolve("cat_test.zip")
            val bytes = ipfs.cat("QmfUFCfDSUXnXpqWJc7UXkG9C5x4PfuSGD7LkPLfEutcL6")
            file.writeBytes(bytes)
        }
    }

    @Test
    fun testLs() {
        runBlocking {
            val ls = ipfs.ls("QmSa4jrvLLZ7SDH5D1aQ5jUVSCssmWMXeqRNuWtfCrUxh9")
            val ls2 = ipfs.ls("QmfUFCfDSUXnXpqWJc7UXkG9C5x4PfuSGD7LkPLfEutcL6")
            println(ls)
            println(ls2)
        }
    }

    @Test
    fun writeFile() {
        runBlocking {
            ipfs.files.writeFile("filetest.txt", "hello world!!!".toByteArray())
        }
    }

    @Test
    fun getDag() {
        runBlocking {
            val dag = ipfs.dag.get("QmSa4jrvLLZ7SDH5D1aQ5jUVSCssmWMXeqRNuWtfCrUxh9")
            val dag2 = ipfs.dag.get("QmfUFCfDSUXnXpqWJc7UXkG9C5x4PfuSGD7LkPLfEutcL6")
        }
    }
}