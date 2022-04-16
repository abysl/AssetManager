package com.abysl.assetmanager.model

import com.abysl.assetmanager.db.tables.AssetTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

data class AssetIndex(
    val id: Int,
    val assetId: Int,
    val file: File,
) {
    val asset by lazy {
        transaction {
            AssetTable
                .select { AssetTable.id eq assetId }
                .map(AssetTable::fromRow)
                .first()
        }
    }
}