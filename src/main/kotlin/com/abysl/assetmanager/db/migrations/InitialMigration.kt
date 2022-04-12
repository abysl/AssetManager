package com.abysl.assetmanager.db.migrations

import com.abysl.assetmanager.db.Migration
import com.abysl.assetmanager.db.tables.VersionTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class InitialMigration() : Migration() {
    override fun up() {
        transaction {
            SchemaUtils.create (VersionTable)

            VersionTable.insert {
                it[current_version] = 0
            }
        }
    }

    override fun down() {

    }
}