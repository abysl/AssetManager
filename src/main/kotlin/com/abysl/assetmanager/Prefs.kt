package com.abysl.assetmanager

import okio.Path.Companion.toPath

object Prefs {

    val HOME_FOLDER = ".abysltcg".toPath().toFile().also { it.mkdir() }
    val IMAGE_FOLDER = (HOME_FOLDER.path + "/images").toPath().toFile().also { it.mkdir() }
    val DB_FILE = (Prefs.HOME_FOLDER.path + "/db.sqlite").toPath().toFile().also { it.createNewFile() }
}