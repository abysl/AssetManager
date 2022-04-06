package com.abysl.assetmanager.db.migrations

import com.abysl.assetmanager.db.Migration
import com.abysl.assetmanager.db.tables.PreferencesTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class PreferencesMigration: Migration() {
    override fun up() {
        transaction {
            SchemaUtils.create(PreferencesTable)
        }
    }

    override fun down() {
        transaction {
            SchemaUtils.drop(PreferencesTable)
        }
    }
}