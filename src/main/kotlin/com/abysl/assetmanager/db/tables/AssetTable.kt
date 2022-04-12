package com.abysl.assetmanager.db.tables

import com.abysl.assetmanager.model.Asset
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object AssetTable: IntIdTable() {
    val name = varchar("name", 256);
    val sourcePlatform = varchar("source_platform", 16)
    val creator = varchar("creator", 256).default("Unknown")
    val iconUrl  = varchar("iconUrl", 256)
    val sourceUrl = varchar("sourceUrl", 256);
    val downloads = varchar("downloads", 4096)

    fun fromRow(row: ResultRow): Asset  =
        Asset(
            name = row[name],
            sourcePlatform = row[sourcePlatform],
            creator = row[creator],
            iconUrl = row[iconUrl],
            sourceUrl = row[sourceUrl],
            downloads = Json.decodeFromString(row[downloads])
        )
}