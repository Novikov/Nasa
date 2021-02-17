package com.nasa.app.data.api.json

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class MediaAssetDeserializer: JsonDeserializer<MediaAssetResponse> {
    private val TAG: String = "MediaAssetDeserialization"
    lateinit var assetType:AssetType
    var assetMap = mapOf<String,String>()

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MediaAssetResponse {

        json?.asJsonObject?.entrySet()?.forEach {
            Log.i(TAG, "Deserialization of MediaDetail begins")

            if (it.key.equals("collection")) {
                Log.i(TAG, "inside collection object")

                it.value.asJsonObject.entrySet()?.forEach {
                    if (it.key.equals("items")) {
                        Log.i(TAG, "inside items object")
                        val items = it.value.asJsonArray

                        assetType = getAssetType(items)

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

        return MediaAssetResponse(assetMap)
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
            if (href.contains("jpg").and(!href.contains("thumb"))) {
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