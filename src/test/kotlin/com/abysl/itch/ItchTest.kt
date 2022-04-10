package com.abysl.itch

import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.services.DBService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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
            println(itch.getKeys())
            println(ItchClientConfig(apiKey = Prefs.itchApiKey ?: "").apiUrl())
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
            val game = itch.getKeys().first()
            val uploads = itch.getUploads(game.first, game.second)
            val test = itch.getDownloadUrl(uploads.first(), game.first)
        }
    }
}