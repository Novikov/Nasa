package com.nasa.app.data.api.json

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class MediaAssetDeserializer: JsonDeserializer<MediaDetailAssetResponse> {
    private val TAG: String = "MediaAssetDeserialization"
    lateinit var assetType:AssetType
    var metadataUrl:String? = null
    var assetMap = mapOf<String,String>()

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MediaDetailAssetResponse {

        json?.asJsonObject?.entrySet()?.forEach {
            Log.i(TAG, "Deserialization of MediaDetail begins")

            if (it.key.equals("collection")) {
                Log.i(TAG, "inside collection object")

                it.value.asJsonObject.entrySet()?.forEach {
                    if (it.key.equals("items")) {
                        Log.i(TAG, "inside items object")
                        val items = it.value.asJsonArray

                        assetType = getAssetType(items)

                        metadataUrl = getMetadataUrl(items)


                        when(assetType){
                            AssetType.AUDIO -> assetMap = getAudioAsset(items)
                            AssetType.VIDEO -> assetMap = getVideoAsset(items)
                            AssetType.IMAGE -> assetMap = getImageAsset(items)
                        }

                        Log.i(TAG, "$assetMap")
                    }
                }
            }
        }

        return MediaDetailAssetResponse(assetMap,metadataUrl!!)
    }

    private fun getMetadataUrl(items: JsonArray): String? {
        var tmpUrl:String? = null
        for (element in items) {
            var href = element.asJsonObject.get("href").asString
            if (href.contains("metadata.json")) {
                tmpUrl = href
            }
        }
        return tmpUrl
    }

    private fun getAudioAsset(items: JsonArray): Map<String, String> {
        val tmpMap = mutableMapOf<String, String>()
        for (element in items) {
            var href = element.asJsonObject.get("href").asString
            if (href.contains("mp3").or(href.contains("m4a"))) {
                val startStringPosition = href.indexOf('~') + 1
                val assetName = href.subSequence(startStringPosition, href.length).toString()
                tmpMap.put(assetName, href)
            }
        }
        return tmpMap
    }

    private fun getVideoAsset(items: JsonArray): Map<String, String> {
        val tmpMap = mutableMapOf<String, String>()
        for (element in items) {
            var href = element.asJsonObject.get("href").asString
            if (href.contains("mp4")) {
                val startStringPosition = href.indexOf('~') + 1
                val assetName = href.subSequence(startStringPosition, href.length).toString()
                tmpMap.put(assetName, href)
            }
        }
        return tmpMap
    }

    private fun getImageAsset(items: JsonArray): Map<String, String> {
        val tmpMap = mutableMapOf<String, String>()
        for (element in items) {
            var href = element.asJsonObject.get("href").asString
            if ((href.contains("jpg").or(href.contains("tif"))).and(!href.contains("thumb"))) {
                val startStringPosition = href.indexOf('~') + 1
                val assetName = href.subSequence(startStringPosition, href.length).toString()
                tmpMap.put(assetName, href)
            }
        }
        return tmpMap
    }

    private fun getAssetType(items: JsonArray) : AssetType {
        var assetType:AssetType? = null

        for (element in items) {
            var href = element.asJsonObject.get("href").asString

            if (href.contains("mp3").or(href.contains("m4a"))) {
                assetType =  AssetType.AUDIO
                break
            } else if (href.contains("mp4")) {
                assetType = AssetType.VIDEO
                break
            }
        }
        return assetType?:AssetType.IMAGE
    }
}