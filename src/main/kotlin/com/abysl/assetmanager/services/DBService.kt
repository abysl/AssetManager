package com.abysl.assetmanager.services

import com.abysl.assetmanager.Prefs.DB_FILE
import com.abysl.assetmanager.db.MigrationService
import org.jetbrains.exposed.sql.*


class DBService {

    init {
        Database.connect("jdbc:sqlite:${DB_FILE.path}", "org.sqlite.JDBC")
        MigrationService().migrateToLatest()
    }

}