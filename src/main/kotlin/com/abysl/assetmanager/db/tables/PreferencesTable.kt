package com.abysl.assetmanager.db.tables

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object PreferencesTable : Table() {
    val key = varchar("key", 256).uniqueIndex()
    val value = varchar("value", 4096)

    operator fun get(pref: String): String? {
        return transaction { PreferencesTable.select { key eq pref }.firstOrNull() }?.get(value)
    }

    operator fun set(pref: String, prefvalue: String) {
        val oldValue = get(pref)
        transaction {
            if(oldValue == null) {
                PreferencesTable.insert {
                    it[key] = pref
                    it[value] = prefvalue
                }
            }else {
                PreferencesTable.update {
                    it[value] = prefvalue
                }
            }
        }
    }
}