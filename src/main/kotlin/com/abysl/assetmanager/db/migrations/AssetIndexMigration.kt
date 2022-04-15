package com.abysl.assetmanager.db.migrations

import com.abysl.assetmanager.db.Migration
import com.abysl.assetmanager.db.tables.AssetIndexTable
import com.abysl.assetmanager.db.tables.AssetTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class AssetIndexMigration: Migration() {
    override fun up() {
        transaction {
            SchemaUtils.create(AssetIndexTable)

            AssetTable.update { it[downloaded] = false }
        }
    }

    override fun down() {
        transaction {
            SchemaUtils.drop(AssetIndexTable)
        }
    }
}