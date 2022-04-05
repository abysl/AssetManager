package com.abysl.humble

import com.abysl.humble.model.DownloadData
import com.abysl.humble.model.DownloadStruct
import com.abysl.humble.model.FakeJson
import com.abysl.humble.model.SubProduct
import kotlinx.serialization.json.*

class Humble {

    fun getProducts(): List<SubProduct> = deserializeJson(Json.parseToJsonElement(FakeJson.response))

    fun deserializeJson(json: JsonElement): List<SubProduct> {
        return json.jsonObject.keys.fold(emptyList()) { acc, s ->
            val subproductArray = json.jsonObject[s]?.jsonObject?.get("subproducts")?.jsonArray ?: return@fold acc
            return@fold acc + parseSubProducts(subproductArray)
        }
    }

    private fun parseSubProducts(el: JsonArray): List<SubProduct>{
        val subproducts = mutableListOf<SubProduct>()
        el.forEach {
            val name = it.jsonObject["human_name"]?.jsonPrimitive?.content ?: return@forEach
            val url = it.jsonObject["url"]?.jsonPrimitive?.content ?: return@forEach
            val icon = it.jsonObject["icon"]?.jsonPrimitive?.content ?: return@forEach
            val downloads = parseDownloads(it.jsonObject["downloads"]?.jsonArray ?: return@forEach)
            subproducts.add(SubProduct(name = name, url = url, icon = icon, downloads = downloads))
        }
        return subproducts
    }

    private fun parseDownloads(el: JsonArray): List<DownloadData> {
        val downloads = mutableListOf<DownloadData>()
        el.forEach {
            val platform = it.jsonObject["platform"]?.jsonPrimitive?.content ?: return@forEach
            val downloadStructs = parseDownloadStructs(it.jsonObject["download_struct"]?.jsonArray ?: return@forEach)
            downloads.add(DownloadData(platform = platform, downloads = downloadStructs))
        }
        return downloads
    }

    private fun parseDownloadStructs(el: JsonArray): List<DownloadStruct> {
        val downloadStructs = mutableListOf<DownloadStruct>()
        el.forEach {
            val name = it.jsonObject["name"]?.jsonPrimitive?.content ?: return@forEach
            val md5 = it.jsonObject["md5"]?.jsonPrimitive?.content ?: return@forEach
            val sha1 = it.jsonObject["sha1"]?.jsonPrimitive?.content ?: return@forEach
            val web = it.jsonObject["url"]?.jsonObject?.get("web")?.jsonPrimitive?.content ?: return@forEach
            val torrent = it.jsonObject["url"]?.jsonObject?.get("bittorrent")?.jsonPrimitive?.content ?: return@forEach
            val humanSize = it.jsonObject["human_size"]?.jsonPrimitive?.content ?: return@forEach
            val fileSize = it.jsonObject["file_size"]?.jsonPrimitive?.long ?: return@forEach
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