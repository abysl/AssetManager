package com.abysl.assetmanager.db.migrations

import com.abysl.assetmanager.db.Migration
import com.abysl.assetmanager.db.tables.AssetTable
import com.abysl.assetmanager.model.Asset
import com.abysl.humble.Humble
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class AssetCreator : Migration() {
    override fun up() {
        val assets = Humble().getProducts().map {
            Asset(
                name = it.name,
                creator = it.creator,
                iconUrl = it.iconUrl,
                sourceUrl = it.url,
                downloads = it.downloads
            )
        }
        transaction {
            SchemaUtils.createMissingTablesAndColumns(AssetTable)
            assets.forEach {asset ->
                AssetTable.update({AssetTable.name eq asset.name }) {
                    it[creator] = asset.creator
                }
            }
        }
    }


    override fun down() {
    }
}