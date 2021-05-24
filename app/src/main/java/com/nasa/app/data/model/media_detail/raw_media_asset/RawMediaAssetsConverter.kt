package com.nasa.app.data.model.media_detail.raw_media_asset

import com.nasa.app.data.model.ContentType
import javax.inject.Inject

class RawMediaAssetsConverter @Inject constructor() {

    private val metadataUriSubstring = "metadata.json"
    private val mp3UriSubstring = "mp3"
    private val m4aUriSubstring = "m4a"
    private val wavUriSubstring = "wav"
    private val mp4UriSubstring = "mp4"
    private val movUriSubstring = "mov"
    private val jpgUriSubstring = "jpg"
    private val tifUriSubstring = "tif"
    private val thumbUriSubstring = "thumb"
    private val dividerSymbol = '~'

    fun getMediaDetailAssetResponse(rawMediaDetailAssetResponse: RawMediaDetailAssetResponse): MediaDetailAssetResponse {

        val assetType = getAssetType(rawMediaDetailAssetResponse.collection.items)

        val assetMap: LinkedHashMap<String, String> = when (assetType) {
            ContentType.AUDIO -> parseAudioAsset(rawMediaDetailAssetResponse.collection.items)
            ContentType.VIDEO -> parseVideoAsset(rawMediaDetailAssetResponse.collection.items)
            ContentType.IMAGE -> parseImageAsset(rawMediaDetailAssetResponse.collection.items)
        }

        return MediaDetailAssetResponse(assetType,assetMap)
    }

    fun getMetadataUrl(rawMediaDetailAssetResponse: RawMediaDetailAssetResponse): String? {
        var tmpUrl: String? = null
        val items = rawMediaDetailAssetResponse.collection.items
        for (element in items) {
            val href = element.href
            if (href.contains(metadataUriSubstring)) {
                tmpUrl = href
            }
        }
        return tmpUrl
    }

    private fun parseAudioAsset(items: List<Item>): LinkedHashMap<String, String> {
        val tmpMap = linkedMapOf<String, String>()

        for (element in items) {
            val href = element.href
            if (href.contains(mp3UriSubstring)
                    .or(href.contains(m4aUriSubstring)
                        .or(href.contains(wavUriSubstring)))
            ) {
                val startStringPosition = href.indexOf(dividerSymbol) + 1
                val assetName = href.subSequence(startStringPosition, href.length).toString()
                tmpMap[assetName] = href
            }
        }
        return tmpMap
    }

    private fun parseVideoAsset(items: List<Item>): LinkedHashMap<String, String> {
        val tmpMap = linkedMapOf<String, String>()
        for (element in items) {
            val href = element.href
            if (href.contains(mp4UriSubstring).or(href.contains(movUriSubstring))) {
                val startStringPosition = href.indexOf(dividerSymbol) + 1
                val assetName = href.subSequence(startStringPosition, href.length).toString()
                tmpMap[assetName] = href
            }
        }
        return tmpMap
    }

    private fun parseImageAsset(items: List<Item>): LinkedHashMap<String, String> {
        val tmpMap = linkedMapOf<String, String>()
        for (element in items) {
            val href = element.href
            if ((href.contains(jpgUriSubstring)
                    .or(href.contains(tifUriSubstring)))
                    .and(!href.contains(thumbUriSubstring))
            ) {
                val startStringPosition = href.indexOf(dividerSymbol) + 1
                val assetName = href.subSequence(startStringPosition, href.length).toString()
                tmpMap.put(assetName, href)
            }
        }
        return tmpMap
    }

   private fun getAssetType(items: List<Item>): ContentType {
        var contentType: ContentType? = null

        for (element in items) {
            val href = element.href
            if (href.contains(mp3UriSubstring)
                    .or(href.contains(m4aUriSubstring)
                        .or(href.contains(wavUriSubstring)))
            ) {
                contentType = ContentType.AUDIO
                break
            } else if (href.contains(mp4UriSubstring)) {
                contentType = ContentType.VIDEO
                break
            }
        }
        return contentType ?: ContentType.IMAGE
    }
}