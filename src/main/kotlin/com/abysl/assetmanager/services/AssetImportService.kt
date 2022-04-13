package com.abysl.assetmanager.services

import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.db.tables.AssetTable
import com.abysl.assetmanager.ui.components.assetimport.SourcePlatform
import com.abysl.humble.model.HumbleProduct
import com.abysl.itch.Itch
import com.abysl.itch.model.ItchGame
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class AssetImportService() {
    fun importItchAssets() {
        val itch = Itch(Prefs.itchApiKey)
        CoroutineScope(Dispatchers.IO).launch {
            val itchGames: List<ItchGame> = runBlocking { itch.getAssets() }
            println(itchGames.maxOf { Prefs.jsonFormat.encodeToString(it).length })

            // Map of itch game ids to asset ids of assets that were already in the db
            val existingItchAssets = transaction {
                AssetTable.select {
                    AssetTable.sourcePlatform eq Prefs.jsonFormat.encodeToString(SourcePlatform.ITCH)
                }.map(AssetTable::fromRow)
            }.map { asset -> (asset.sourceObject as ItchGame).id to asset.id }.toMap()

            transaction {
                for (game in itchGames) {
                    // if asset is pre-existing, update it
                    if (game.id in existingItchAssets.keys) {
                        AssetTable.update({ AssetTable.id eq existingItchAssets[game.id] }) {
                            it[name] = game.title
                            it[creator] = game.creator.username
                            it[downloaded] = false
                            it[sourcePlatform] = Prefs.jsonFormat.encodeToString(SourcePlatform.ITCH)
                            it[sourceData] = Prefs.jsonFormat.encodeToString(game)
                        }
                    } else {
                        AssetTable.insert {
                            it[name] = game.title
                            it[creator] = game.creator.username
                            it[downloaded] = false
                            it[sourcePlatform] = Prefs.jsonFormat.encodeToString(SourcePlatform.ITCH)
                            it[sourceData] = Prefs.jsonFormat.encodeToString(game)
                        }
                    }
                }
            }
        }
    }

    fun importHumbleAssets(assets: List<HumbleProduct>) {
        transaction {
            AssetTable.batchInsert(assets) {
                this[AssetTable.name] = it.name
                this[AssetTable.creator] = it.creator
                this[AssetTable.downloaded] = false
                this[AssetTable.sourcePlatform] = Prefs.jsonFormat.encodeToString(SourcePlatform.HUMBLE)
                this[AssetTable.sourceData] = Prefs.jsonFormat.encodeToString(it)
            }
        }
    }
}