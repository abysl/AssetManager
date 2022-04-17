package com.abysl.assetmanager

import com.abysl.assetmanager.db.tables.PreferencesTable
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath
import java.io.File

object Prefs {

    val USER_FOLDER = System.getProperty("user.home").toPath().toFile()
    val HOME_FOLDER = USER_FOLDER.resolve(".abysl/assetmanager").also { it.mkdirs() }
    val DB_FILE = HOME_FOLDER.resolve("assetmanager.sqlite").also { it.createNewFile() }
    val IMAGE_PATH = HOME_FOLDER.resolve("images").also { it.mkdirs() }
    val IMAGE_CACHE = IMAGE_PATH.resolve("cache").also { it.mkdirs() }
    val ASSET_PATH = HOME_FOLDER.resolve("assets")

    val jsonFormat = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    var itchApiKey: String
        get() = PreferencesTable["itchApiKey"] ?: ""
        set(value) {
            PreferencesTable["itchApiKey"] = value
        }

    var darkMode: Boolean
        get() = PreferencesTable["darkMode"] != "0"
        set(value) {
            PreferencesTable["darkMode"] = if (value) "1" else "0"
        }

}