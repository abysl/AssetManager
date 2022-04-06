package com.abysl.assetmanager.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object PreferencesTable: IntIdTable() {
    val key = varchar("key", 256)
    val value = varchar("value",4096)

    operator fun get(pref: String): String? {
        return transaction { PreferencesTable.select { key eq pref }.firstOrNull() }?.get(value)
    }

    operator fun set(pref: String, prefvalue: String){
        transaction {
            PreferencesTable.insert {
                it[key] = pref
                it[value] = prefvalue
            }
        }
    }
}