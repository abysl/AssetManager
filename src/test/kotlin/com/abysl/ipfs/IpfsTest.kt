package com.abysl.ipfs

import com.abysl.ipfs.network.endpoints.Ipfs
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
            val fileStore = "ipfs/".toPath().toFile().also {  it.mkdir() }
            val file = "ipfs/get_test.zip".toPath().toFile().also { it.createNewFile()}
            val byteReader = ipfs.get("QmfUFCfDSUXnXpqWJc7UXkG9C5x4PfuSGD7LkPLfEutcL6")
            withContext(Dispatchers.IO){
                while(byteReader.availableForRead > 0) {
                    val buffer = ByteArray(byteReader.availableForRead)
                    byteReader.readAvailable(buffer)
                    file.writeBytes(buffer)
                }
            }
        }
    }

    @Test
    fun cat(){
        runBlocking {
            val fileStore = "ipfs/".toPath().toFile().also {  it.mkdir() }
            val file = "ipfs/cat_test.zip".toPath().toFile().also { it.createNewFile()}
            val byteReader = ipfs.cat("QmfUFCfDSUXnXpqWJc7UXkG9C5x4PfuSGD7LkPLfEutcL6")
            withContext(Dispatchers.IO){
                while(byteReader.availableForRead > 0) {
                    val buffer = ByteArray(byteReader.availableForRead)
                    byteReader.readAvailable(buffer)
                    file.writeBytes(buffer)
                }
            }

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