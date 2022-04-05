package com.abysl.assetmanager.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object VersionTable: IntIdTable() {
    val current_version = integer("current_version")

    fun fromRow(row: ResultRow): Int =  row[current_version]
}