package com.abysl.humble

import com.abysl.humble.model.DownloadStruct
import com.abysl.humble.model.HumbleProduct
import kotlinx.serialization.json.*

class Humble {

    fun deserializeJson(json: JsonElement): List<HumbleProduct> {
        return json.jsonObject.keys.fold(emptyList()) { acc, s ->
            val subproductArray = json.jsonObject[s]?.jsonObject?.get("subproducts")?.jsonArray ?: return@fold acc
            return@fold acc + parseSubProducts(subproductArray)
        }
    }

    private fun parseSubProducts(el: JsonArray): List<HumbleProduct> {
        val subproducts = mutableListOf<HumbleProduct>()
        el.forEach {
            val name = it.jsonObject["human_name"]!!.jsonPrimitive.content
            val url = it.jsonObject["url"]!!.jsonPrimitive.content
            val icon = it.jsonObject["icon"]!!.jsonPrimitive.content
            val creator = it.jsonObject["payee"]!!.jsonObject["human_name"]!!.jsonPrimitive.content
            val downloads = parseDownloads(it.jsonObject["downloads"]!!.jsonArray)
            subproducts.add(
                HumbleProduct(
                    name = name,
                    url = url,
                    iconUrl = icon,
                    creator = creator,
                    downloads = downloads
                )
            )
        }
        return subproducts
    }

    private fun parseDownloads(el: JsonArray): List<DownloadStruct> {
        val downloads = mutableListOf<DownloadStruct>()
        el.forEach {
            val downloadStructs: List<DownloadStruct> =
                parseDownloadStructs(it.jsonObject["download_struct"]!!.jsonArray)
            downloads.addAll(downloadStructs)
        }
        return downloads
    }

    private fun parseDownloadStructs(el: JsonArray): List<DownloadStruct> {
        val downloadStructs = mutableListOf<DownloadStruct>()
        el.forEach {
            val name = it.jsonObject["name"]!!.jsonPrimitive.content
            val md5 = it.jsonObject["md5"]!!.jsonPrimitive.content
            val sha1 = it.jsonObject["sha1"]?.jsonPrimitive?.content ?: ""
            val web = it.jsonObject["url"]!!.jsonObject["web"]!!.jsonPrimitive.content
            val torrent = it.jsonObject["url"]!!.jsonObject["bittorrent"]!!.jsonPrimitive.content
            val humanSize = it.jsonObject["human_size"]!!.jsonPrimitive.content
            val fileSize = it.jsonObject["file_size"]!!.jsonPrimitive.long
            downloadStructs.add(
                DownloadStruct(
                    name = name,
                    md5 = md5,
                    sha1 = sha1,
                    web = web,
                    torrent = torrent,
                    humanSize = humanSize,
                    fileSize = fileSize
                )
            )
        }
        return downloadStructs
    }
}