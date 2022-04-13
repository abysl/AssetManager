package com.abysl.assetmanager.db.tables

import com.abysl.assetmanager.model.Asset
import com.abysl.assetmanager.model.Tag
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object TagTable: IntIdTable() {
    val name = varchar("name", 256)
    val assetId = integer("asset_id")

    fun fromRow(row: ResultRow): Tag  =
        Tag(
            name = row[name],
        )
}