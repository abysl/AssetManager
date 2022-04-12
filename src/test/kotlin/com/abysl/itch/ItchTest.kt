package com.abysl.itch

import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.services.DBService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class ItchTest {

    init {
        DBService()
    }
    val itch = Itch(ItchClientConfig(apiKey = Prefs.itchApiKey ?: ""))

    @Test
    fun getProfile() {
        runBlocking {
            println(itch.getProfile())
        }
    }

    @Test
    fun getKeys() {
        runBlocking {
            assert(itch.getAssets().isNotEmpty())
        }
    }

    @Test
    fun getCsrf(){
        runBlocking {
            println(itch.getCsrf("https://itch.io"))
        }
    }

    @Test
    fun login(){

    }

    @Test
    fun getDownloadUrl(){
        runBlocking {
            // step one login
            // step two visit game page
            // step 3 get download page url
            // step 4 extract file link
            // step 5 request file link
            // step 6 request file from cdn
            val assets = itch.getAssets()
            val test = itch.getDownloadUrls(assets.keys.first())
            println(test)
        }
    }
}