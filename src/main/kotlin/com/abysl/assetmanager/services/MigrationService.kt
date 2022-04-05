package com.abysl.assetmanager.services

import com.abysl.assetmanager.db.Migration
import com.abysl.assetmanager.db.migrations.InitialSchema
import com.abysl.assetmanager.db.tables.VersionTable
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class MigrationService {

    private val migrations: HashMap<Int, Migration> = hashMapOf()

    init {
        registerMigrations()
    }

    fun runMigrations(desiredVersion: Int) {
        val currentVersion =
            if (transaction { !VersionTable.exists() }) -1
            else VersionTable.fromRow(transaction { VersionTable.selectAll().first() })

        val versionDifference = desiredVersion - currentVersion

        if (versionDifference == 0)
            return

        if (versionDifference > 0) {
            migrations[currentVersion + 1]?.up()
        } else {
            migrations[currentVersion]?.down()
        }

        runMigrations(desiredVersion)
    }

    fun migrateToLatest() {
        runMigrations(migrations.keys.last())
    }

    private fun registerMigrations() {
        migrations[0] = InitialSchema()
    }
}