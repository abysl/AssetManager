package com.abysl.assetmanager.db.tables

import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.model.Asset
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object AssetTable: IntIdTable() {
    val name = varchar("name", 256);
    val creator = varchar("creator", 256).default("unknown")
    val downloaded = bool("downloaded")
    val sourcePlatform = varchar("source_platform", 16)
    val sourceData = varchar("source_data", 8192)

    fun fromRow(row: ResultRow): Asset  =
        Asset(
            id = row[id].value,
            name = row[name],
            creator = row[creator],
            downloaded = row[downloaded],
            sourcePlatform = Prefs.jsonFormat.decodeFromString(row[sourcePlatform]),
            sourceData = row[sourceData]
        )
}