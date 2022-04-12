package com.abysl.itch

import com.abysl.itch.model.ItchGame
import com.abysl.itch.model.ItchGameKey
import com.abysl.itch.model.ItchUpload
import com.abysl.itch.model.ItchUser
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import org.jsoup.Jsoup
import java.io.File
import java.util.stream.Collectors

class Itch(val config: ItchClientConfig) {
    val jsonFormat = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    val client = HttpClient(CIO) {
        followRedirects = false
        expectSuccess = false
        install(ContentNegotiation) {
            json(jsonFormat)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.NONE
        }
        install(HttpCookies)
        install(HttpSend)
    }

    val base = config.apiUrl()

    suspend fun login(username: String, password: String) {
        val csrf = getCsrf("https://itch.io/login")
        println(csrf)
        val response = client.submitForm(
            "https://itch.io/login",
            Parameters.build {
                append("csrf_token", csrf)
                append("username", username)
                append("password", password)
            })
    }

    suspend fun getCsrf(url: String): String {
        val page: HttpResponse = client.get(url)
        val doc = Jsoup.parse(page.bodyAsText())
        return doc.select("meta").first { it.attr("name") == "csrf_token" }.`val`()
    }

    suspend fun getProfile(page: Int = 0): ItchUser {
        val response: HttpResponse = client.get("$base/me") {
            parameter("page", page)
        }
        val json = jsonFormat.parseToJsonElement(response.bodyAsText()).jsonObject["user"]!!.jsonObject
        return Json.decodeFromJsonElement(json)
    }

    suspend fun getAssets(): Map<ItchGameKey, ItchGame> {
        val response: HttpResponse = client.get("$base/my-owned-keys")
        val games: Map<ItchGameKey, ItchGame> = jsonFormat
            .parseToJsonElement(response.bodyAsText())
            .jsonObject["owned_keys"]!!.jsonArray.map {
            val game: ItchGame = jsonFormat.decodeFromJsonElement(it.jsonObject["game"]!!)
            val key = it.jsonObject["id"]!!.jsonPrimitive.content
            return@map ItchGameKey(game.id, key) to game
        }.toMap()
        return games
    }

    suspend fun getDownloadUrls(key: ItchGameKey): List<String> {
        val uuid: String = getUuid(key.gameId)
        val uploads = getUploads(key)
        val urls = uploads.parallelStream().map { upload ->
            runBlocking {
                return@runBlocking getDownloadUrl(key.gameKey, upload, uuid)
            }
        }.collect(Collectors.toUnmodifiableList())
        return urls.toTypedArray().toList()
    }

    suspend fun getDownloadUrl(
        key: String,
        upload: ItchUpload,
        uuid: String = runBlocking { getUuid(upload.gameId) }
    ): String {
        val download = client.get("https://api.itch.io/uploads/${upload.id}/download") {
            parameter("api_key", config.apiKey)
            parameter("download_key_id", key)
            parameter("uuid", uuid)
        }
        return download.headers["Location"].toString()
    }

    suspend fun getUuid(gameId: ULong): String {
        return Json.parseToJsonElement(client.post("https://api.itch.io/games/$gameId/download-sessions") {
            headers { append("Authorization", config.apiKey) }
        }.bodyAsText()).jsonObject["uuid"]!!.jsonPrimitive.content
    }

    suspend fun getUploads(key: ItchGameKey): List<ItchUpload> {
        val uploads = jsonFormat.parseToJsonElement(
            client.get("https://api.itch.io/games/${key.gameId}/uploads") {
                parameter("download_key_id", key.gameKey)
                headers { append("Authorization", config.apiKey) }
            }.bodyAsText()
        )
        return uploads.jsonObject["uploads"]!!.jsonArray.map { jsonFormat.decodeFromJsonElement(it) }
    }

    suspend fun downloadFile(url: String, downloadLocation: File, overwrite: Boolean = false) {
        val fileBytes = HttpClient(CIO).get(url).readBytes()
        if (downloadLocation.exists() && !overwrite) return
        downloadLocation.parentFile?.mkdirs()
        downloadLocation.writeBytes(fileBytes)
    }
}