package com.abysl.assetmanager.db.tables

import com.abysl.assetmanager.model.GameCard
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object CardTable: IntIdTable() {
    val name = varchar("name", 256);
    val scryfall_id = varchar("scryfall_id", 36).uniqueIndex()
    val image_path  = varchar("image_path", 256)

    fun fromRow(row: ResultRow): GameCard  =
        GameCard(
            name = row[name],
            scryfall_id = row[scryfall_id],
            image_path = row[image_path]
        )
}