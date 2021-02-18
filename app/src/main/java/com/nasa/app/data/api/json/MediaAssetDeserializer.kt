package com.nasa.app.data.api.json

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.nasa.app.data.model.ContentType
import java.lang.reflect.Type

class MediaAssetDeserializer: JsonDeserializer<MediaDetailAssetResponse> {
    private val TAG: String = "MediaAssetDeserialization"
    lateinit var contentType: ContentType
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

                        contentType = getAssetType(items)

                        metadataUrl = getMetadataUrl(items)


                        when(contentType){
                            ContentType.AUDIO -> assetMap = getAudioAsset(items)
                            ContentType.VIDEO -> assetMap = getVideoAsset(items)
                            ContentType.IMAGE -> assetMap = getImageAsset(items)
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

    private fun getAudioAsset(items: JsonArray): LinkedHashMap<String, String>  {
        val tmpMap = linkedMapOf<String, String>()
        for (element in items) {
            var href = element.asJsonObject.get("href").asString
            if (href.contains("mp3").or(href.contains("m4a").or(href.contains("wav")))) {
                val startStringPosition = href.indexOf('~') + 1
                val assetName = href.subSequence(startStringPosition, href.length).toString()
                tmpMap.put(assetName, href)
            }
        }
        return tmpMap
    }

    private fun getVideoAsset(items: JsonArray): LinkedHashMap<String, String> {
        val tmpMap = linkedMapOf<String, String>()
        for (element in items) {
            var href = element.asJsonObject.get("href").asString
            if (href.contains("mp4").or(href.contains("mov"))) {
                val startStringPosition = href.indexOf('~') + 1
                val assetName = href.subSequence(startStringPosition, href.length).toString()
                tmpMap.put(assetName, href)
            }
        }
        return tmpMap
    }

    private fun getImageAsset(items: JsonArray): LinkedHashMap<String, String>  {
        val tmpMap = linkedMapOf<String, String>()
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

    private fun getAssetType(items: JsonArray) : ContentType {
        var contentType: ContentType? = null

        for (element in items) {
            var href = element.asJsonObject.get("href").asString

            if (href.contains("mp3").or(href.contains("m4a").or(href.contains("wav")))) {
                contentType =  ContentType.AUDIO
                break
            } else if (href.contains("mp4")) {
                contentType = ContentType.VIDEO
                break
            }
        }
        return contentType?: ContentType.IMAGE
    }
}