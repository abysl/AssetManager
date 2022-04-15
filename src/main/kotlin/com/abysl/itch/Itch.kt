package com.abysl.itch

import com.abysl.assetmanager.Prefs
import com.abysl.itch.model.ItchGame
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
    constructor(apiKey: String) : this(ItchClientConfig(apiKey))

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
        return Prefs.jsonFormat.decodeFromJsonElement(json)
    }

    suspend fun getAssets(): List<ItchGame> {
        val response: HttpResponse = client.get("$base/my-owned-keys")
        val gamesJson: List<JsonElement> =
            jsonFormat.parseToJsonElement(response.bodyAsText()).jsonObject["owned_keys"]!!.jsonArray
        val games = gamesJson.map {
            val game: ItchGame = jsonFormat.decodeFromJsonElement(it.jsonObject["game"]!!)
            val key = it.jsonObject["id"]!!.jsonPrimitive.content
            return@map game.copy(key = key)
        }
        return games
    }

    suspend fun getDownloadUrls(game: ItchGame): Map<ItchUpload, String> {
        val uuid: String = getUuid(game.id)
        val uploads = getUploads(game)
        return uploads.map { upload ->
            upload to getDownloadUrl(game.key, upload, uuid)
        }.toMap()
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

    suspend fun getUploads(game: ItchGame): List<ItchUpload> {
        val uploads = jsonFormat.parseToJsonElement(
            client.get("https://api.itch.io/games/${game.id}/uploads") {
                parameter("download_key_id", game.key)
                headers { append("Authorization", config.apiKey) }
            }.bodyAsText()
        )
        return uploads.jsonObject["uploads"]?.jsonArray?.map { jsonFormat.decodeFromJsonElement(it) } ?: emptyList()
    }

    suspend fun downloadFile(url: String, downloadLocation: File, overwrite: Boolean = false) {
        val fileBytes = HttpClient(CIO).get(url).readBytes()
        if (downloadLocation.exists() && !overwrite) return
        downloadLocation.parentFile?.mkdirs()
        downloadLocation.writeBytes(fileBytes)
    }

    companion object {
        private val jsonFormat = Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }

        private val client = HttpClient(CIO) {
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
    }
}