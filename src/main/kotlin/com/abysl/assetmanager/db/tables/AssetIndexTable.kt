package com.abysl.assetmanager.db.tables

import com.abysl.assetmanager.model.AssetIndex
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import java.io.File

object AssetIndexTable: IntIdTable() {
    val assetId = integer("asset_id")
    var filePath = varchar("file_path", 4096)

    fun fromRow(row: ResultRow): AssetIndex =
        AssetIndex(
            id = row[id].value,
            assetId = row[assetId],
            file = File(row[filePath])
        )
}