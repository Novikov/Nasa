package com.nasa.app.data.model.media_detail.raw_media_asset

import com.nasa.app.data.model.ContentType
import javax.inject.Inject

class RawMediaAssetsConverter @Inject constructor() {

    fun getAssets(rawMediaDetailAssetResponse: RawMediaDetailAssetResponse): LinkedHashMap<String, String> {
        var assetMap: LinkedHashMap<String, String>

        val assetType = getAssetType(rawMediaDetailAssetResponse.collection.items)

        assetMap = when (assetType) {
            ContentType.AUDIO -> getAudioAsset(rawMediaDetailAssetResponse.collection.items)
            ContentType.VIDEO -> getVideoAsset(rawMediaDetailAssetResponse.collection.items)
            ContentType.IMAGE -> getImageAsset(rawMediaDetailAssetResponse.collection.items)
        }

        return assetMap
    }

    fun getMetadataUrl(rawMediaDetailAssetResponse: RawMediaDetailAssetResponse): String? {
        var tmpUrl: String? = null
        val items = rawMediaDetailAssetResponse.collection.items
        for (element in items) {
            var href = element.href
            if (href.contains("metadata.json")) {
                tmpUrl = href
            }
        }
        return tmpUrl
    }

    private fun getAudioAsset(items: List<Item>): LinkedHashMap<String, String> {
        val tmpMap = linkedMapOf<String, String>()

        for (element in items) {
            var href = element.href
            if (href.contains("mp3").or(href.contains("m4a").or(href.contains("wav")))) {
                val startStringPosition = href.indexOf('~') + 1
                val assetName = href.subSequence(startStringPosition, href.length).toString()
                tmpMap.put(assetName, href)
            }
        }
        return tmpMap
    }

    private fun getVideoAsset(items: List<Item>): LinkedHashMap<String, String> {
        val tmpMap = linkedMapOf<String, String>()
        for (element in items) {
            var href = element.href
            if (href.contains("mp4").or(href.contains("mov"))) {
                val startStringPosition = href.indexOf('~') + 1
                val assetName = href.subSequence(startStringPosition, href.length).toString()
                tmpMap.put(assetName, href)
            }
        }
        return tmpMap
    }

    private fun getImageAsset(items: List<Item>): LinkedHashMap<String, String> {
        val tmpMap = linkedMapOf<String, String>()
        for (element in items) {
            var href = element.href
            if ((href.contains("jpg").or(href.contains("tif"))).and(!href.contains("thumb"))) {
                val startStringPosition = href.indexOf('~') + 1
                val assetName = href.subSequence(startStringPosition, href.length).toString()
                tmpMap.put(assetName, href)
            }
        }
        return tmpMap
    }

    private fun getAssetType(items: List<Item>): ContentType {
        var contentType: ContentType? = null

        for (element in items) {
            var href = element.href
            if (href.contains("mp3").or(href.contains("m4a").or(href.contains("wav")))) {
                contentType = ContentType.AUDIO
                break
            } else if (href.contains("mp4")) {
                contentType = ContentType.VIDEO
                break
            }
        }
        return contentType ?: ContentType.IMAGE
    }
}