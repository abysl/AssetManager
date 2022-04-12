package com.abysl.assetmanager

import com.abysl.assetmanager.db.tables.PreferencesTable
import okio.Path.Companion.toPath

object Prefs {

    val HOME_FOLDER = System.getProperty("user.home").toPath().toFile()
        .resolve(".abysl/assetmanager")
        .also { it.mkdirs() }
    val DB_FILE = HOME_FOLDER.resolve("assetmanager.sqlite").also { it.createNewFile() }
    val IMAGE_PATH = HOME_FOLDER.resolve("images").also { it.mkdirs() }
    val IMAGE_CACHE = IMAGE_PATH.resolve("cache").also { it.mkdirs() }

    var itchApiKey: String
        get() = PreferencesTable["itchApiKey"] ?: ""
        set(value) {
            PreferencesTable["itchApiKey"] = value
        }

    var darkMode: Boolean
        get() = PreferencesTable["darkMode"] != "0"
        set(value) {
            PreferencesTable["darkMode"] = if(value) "1" else "0"
        }
}