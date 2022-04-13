package com.abysl.assetmanager.db.migrations

import com.abysl.assetmanager.db.Migration
import com.abysl.assetmanager.db.tables.AssetTable
import com.abysl.assetmanager.db.tables.ImageTable
import com.abysl.assetmanager.model.Asset
import com.abysl.assetmanager.ui.components.assetimport.SourcePlatform
import com.abysl.humble.Humble
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction

class AssetTableMigration: Migration() {
    override fun up() {
        transaction {
            SchemaUtils.create (AssetTable)
            SchemaUtils.create (ImageTable)
        }
    }

    override fun down() {
        transaction {
            SchemaUtils.drop (AssetTable)
        }
    }
}