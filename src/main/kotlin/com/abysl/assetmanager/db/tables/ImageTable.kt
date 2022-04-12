package com.abysl.assetmanager.db.tables

import com.abysl.assetmanager.model.CachedImage
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object ImageTable: IntIdTable() {
    val url = varchar("name", 256)

    fun fromRow(row: ResultRow): CachedImage =
        CachedImage(
            url = row[url],
            id = row[id].value,
        )
}