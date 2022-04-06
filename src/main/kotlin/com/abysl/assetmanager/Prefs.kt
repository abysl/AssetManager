package com.abysl.assetmanager

import com.abysl.assetmanager.db.tables.PreferencesTable
import kotlinx.coroutines.selects.select
import okio.Path.Companion.toPath
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Prefs {

    val HOME_FOLDER = ".abysl/assetmanager".toPath().toFile().also { it.mkdir() }
    val DB_FILE = HOME_FOLDER.resolve("assetmanager.sqlite").also { it.createNewFile() }
    val IMAGE_PATH = HOME_FOLDER.resolve("images").also { it.mkdirs() }
    val IMAGE_CACHE = IMAGE_PATH.resolve("cache").also { it.mkdirs() }

    val itchApiKey by lazy { PreferencesTable["itchApiKey"] }
}