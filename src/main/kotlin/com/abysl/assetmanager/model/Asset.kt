package com.abysl.assetmanager.model

import androidx.compose.ui.graphics.ImageBitmap
import com.abysl.assetmanager.Prefs
import com.abysl.assetmanager.db.tables.AssetTable
import com.abysl.assetmanager.db.tables.TagTable
import com.abysl.assetmanager.ui.components.assetimport.SourcePlatform
import com.abysl.assetmanager.util.loadLocalImage
import com.abysl.humble.model.HumbleProduct
import com.abysl.itch.model.ItchGame
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.io.File

@kotlinx.serialization.Serializable
data class Asset(
    val id: Int,
    val name: String,
    val creator: String,
    val downloaded: Boolean,
    val sourcePlatform: SourcePlatform,
    val sourceData: String,
) {

    @Transient
    val sourceObject: Any? =
        when (sourcePlatform) {
            SourcePlatform.HUMBLE -> Prefs.jsonFormat.decodeFromString<HumbleProduct>(sourceData)
            SourcePlatform.ITCH -> Prefs.jsonFormat.decodeFromString<ItchGame>(sourceData)
            else -> null
        }


    @Transient
    val dir = Prefs.ASSET_PATH.resolve("$id").also { it.mkdirs() }

    fun getIcon(): ImageBitmap {
        val path = dir.resolve("icon.png")
        val image = loadLocalImage(path)
        return image
    }

    fun getTags(): List<Tag> {
        val asset = id
        return transaction {
            TagTable.select { TagTable.assetId eq asset }
        }.map(TagTable::fromRow)
    }

    fun markDownloaded(dl: Boolean) {
        val asset = id
        transaction {
            AssetTable.update({ AssetTable.id eq asset }) {
                it[downloaded] = dl
            }
        }
    }
}
