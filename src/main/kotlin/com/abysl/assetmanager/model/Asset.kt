package com.abysl.assetmanager.model

import androidx.compose.ui.graphics.ImageBitmap
import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.db.tables.TagTable
import com.abysl.assetmanager.ui.components.assetimport.SourcePlatform
import com.abysl.assetmanager.util.loadLocalImage
import com.abysl.humble.model.HumbleProduct
import com.abysl.itch.model.ItchGame
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

@kotlinx.serialization.Serializable
data class Asset(
    val id: Int,
    val name: String,
    val creator: String,
    val downloaded: Boolean,
    val sourcePlatform: SourcePlatform,
    val sourceData: String,
){

    fun getIcon(): ImageBitmap {
        return loadLocalImage(Prefs.ASSET_PATH.resolve("$id/icon.png"))
    }

    fun getFile(): File {
        return Prefs.ASSET_PATH.resolve("$id")
    }

    suspend fun getTags(): List<Tag> {
        val asset = id
        return transaction {
            TagTable.select { TagTable.assetId eq asset }
        }.map(TagTable::fromRow)
    }

    val sourceObject by lazy {
        when(sourcePlatform){
            SourcePlatform.HUMBLE -> Prefs.jsonFormat.decodeFromString<HumbleProduct>(sourceData)
            SourcePlatform.ITCH -> Prefs.jsonFormat.decodeFromString<ItchGame>(sourceData)
            else -> null
        }
    }
}
