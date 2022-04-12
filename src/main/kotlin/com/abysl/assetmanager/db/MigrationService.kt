package com.abysl.assetmanager.db

import com.abysl.assetmanager.db.migrations.*
import com.abysl.assetmanager.db.tables.VersionTable
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.insert
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
            else VersionTable.fromRow(transaction { VersionTable.selectAll().last() })

        val versionDifference = desiredVersion - currentVersion

        if (versionDifference == 0)
            return

        if (versionDifference > 0) {
            val newVersion = currentVersion + 1
            migrations[newVersion]?.up()
            setVersion(newVersion)
        } else {
            val newVersion = currentVersion - 1;
            migrations[currentVersion]?.down()
            setVersion(newVersion)
        }

        runMigrations(desiredVersion)
    }

    private fun setVersion(version: Int){
        transaction {
            VersionTable.insert { it[current_version] = version }
        }
    }

    fun migrateToLatest() {
        runMigrations(migrations.keys.last())
    }

    private fun registerMigrations() {
        migrations[0] = InitialMigration()
        migrations[1] = AssetTableMigration()
        migrations[2] = PreferencesMigration()
    }
}