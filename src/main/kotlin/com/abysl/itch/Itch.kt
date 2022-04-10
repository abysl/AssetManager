package com.abysl.itch

import com.abysl.itch.model.ItchGame
import com.abysl.itch.model.ItchUpload
import com.abysl.itch.model.ItchUser
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.*
import org.jsoup.Jsoup
import java.io.File

class Itch(val config: ItchClientConfig) {
    val jsonFormat = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    val client = HttpClient(CIO) {
        followRedirects = false
        expectSuccess = false
        install(JsonFeature) {
            serializer = KotlinxSerializer(jsonFormat)
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
        val response = client.submitForm<HttpResponse>(
            "https://itch.io/login",
            Parameters.build {
                append("csrf_token", csrf)
                append("username", username)
                append("password", password)
            })
    }

    suspend fun getCsrf(url: String): String {
        val page: HttpResponse = client.get(url)
        val doc = Jsoup.parse(page.readText())
        return doc.select("meta").first { it.attr("name") == "csrf_token" }.`val`()
    }

    suspend fun getProfile(page: Int = 0): ItchUser {
        val response: HttpResponse = client.get("$base/me") {
            parameter("page", page)
        }
        val json = jsonFormat.parseToJsonElement(response.readText()).jsonObject["user"]!!.jsonObject
        return Json.decodeFromJsonElement(json)
    }

    suspend fun getKeys(): List<Pair<String, ItchGame>> {
        val response: HttpResponse = client.get("$base/my-owned-keys")
        val games: List<Pair<String, ItchGame>> = jsonFormat.parseToJsonElement(response.readText())
            .jsonObject["owned_keys"]!!.jsonArray
            .map {
                it.jsonObject["id"]!!.jsonPrimitive.content to
                        jsonFormat.decodeFromJsonElement(it.jsonObject["game"]!!)
            }
        return games
    }

    suspend fun getDownloadUrl(upload: ItchUpload, gameKey: String): String {
        val uuid: String =
            Json.parseToJsonElement(client.post<HttpResponse>("https://api.itch.io/games/${upload.gameId}/download-sessions") {
                headers { append("Authorization", config.apiKey) }
            }.readText()).jsonObject["uuid"]!!.jsonPrimitive.content

        val download  =
            client.get<HttpResponse>("https://api.itch.io/uploads/${upload.id}/download"){
                parameter("api_key", config.apiKey)
                parameter("download_key_id", gameKey)
                parameter("uuid", uuid)
            }
        return download.headers["Location"].toString()
    }

    suspend fun getUploads(downloadKey: String, game: ItchGame): List<ItchUpload> {
        val uploads = jsonFormat.parseToJsonElement(
            client.get<HttpResponse>("https://api.itch.io/games/${game.id}/uploads"){
            parameter("download_key_id", downloadKey)
            headers { append("Authorization", config.apiKey) }
        }.readText())
        return uploads.jsonObject["uploads"]!!.jsonArray.map { jsonFormat.decodeFromJsonElement(it) }
    }

    suspend fun downloadFile(url: String, downloadLocation: File, overwrite: Boolean = false) {
        val byteReader = HttpClient(CIO).get<HttpResponse>(url).content
        if (downloadLocation.exists() && !overwrite) return
        downloadLocation.parentFile?.mkdirs()
        while (byteReader.availableForRead > 0) {
            val buffer = ByteArray(byteReader.availableForRead)
            byteReader.readAvailable(buffer)
            downloadLocation.writeBytes(buffer)
        }
    }
}