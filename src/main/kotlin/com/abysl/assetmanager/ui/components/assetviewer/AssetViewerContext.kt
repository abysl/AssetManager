package com.abysl.assetmanager.ui.components.assetviewer

import com.abysl.assetmanager.db.tables.AssetTable
import com.abysl.assetmanager.model.Asset
import com.abysl.assetmanager.services.DBService
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class AssetViewerContext {
    fun getAssets(): List<Asset> {
        return transaction { AssetTable.selectAll().map(AssetTable::fromRow)  }
    }
}