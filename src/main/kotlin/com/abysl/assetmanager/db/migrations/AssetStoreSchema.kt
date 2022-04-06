package com.abysl.assetmanager.db.migrations

import com.abysl.assetmanager.db.Migration
import com.abysl.assetmanager.db.tables.AssetTable
import com.abysl.assetmanager.db.tables.AssetTable.downloads
import com.abysl.assetmanager.db.tables.AssetTable.iconUrl
import com.abysl.assetmanager.db.tables.AssetTable.name
import com.abysl.assetmanager.db.tables.AssetTable.sourceUrl
import com.abysl.assetmanager.db.tables.ImageTable
import com.abysl.assetmanager.model.Asset
import com.abysl.humble.Humble
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class AssetStoreSchema: Migration() {
    override fun up() {
        transaction {
            SchemaUtils.create (AssetTable)
            SchemaUtils.create (ImageTable)

            transaction {
                val assets = Humble().getProducts().map {
                    Asset(
                        name = it.name,
                        creator = it.creator,
                        iconUrl = it.iconUrl,
                        sourceUrl = it.url,
                        downloads = it.downloads
                ) }
                AssetTable.batchInsert(assets) { asset ->
                    this[name] = asset.name
                    this[iconUrl] = asset.iconUrl
                    this[sourceUrl] = asset.sourceUrl
                    this[downloads] = Json.encodeToString(asset.downloads)
                }
            }
        }
    }

    override fun down() {
        transaction {
            SchemaUtils.drop (AssetTable)
        }
    }
}