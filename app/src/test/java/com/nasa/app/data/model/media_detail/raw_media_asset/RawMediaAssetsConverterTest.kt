package com.nasa.app.data.model.media_detail.raw_media_asset

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.nasa.app.data.model.ContentType
import org.junit.Test
import java.io.FileReader
import java.util.regex.Pattern


class RawMediaAssetsConverterTest {

    /**
     * 1.Convert should identify asset type
     * 2.Converter should parse asset urls
     */

    val videoAssetJsonResponsePath = "src/test/resources/media_assets/VideoAssets.json"
    val imageAssetJsonResponsePath = "src/test/resources/media_assets/ImageAssets.json"
    val audioAssetJsonResponsePath = "src/test/resources/media_assets/AudioAssets.json"
    val assetsResponseConverter: RawMediaAssetsConverter = RawMediaAssetsConverter()

    @Test
    fun `converter should identify mp3, m4a and waw formats like a audio type`(){
        val rawAssetsResponse = getResponseFromJson(audioAssetJsonResponsePath)
        val mediaDetailAssetsResponse =  assetsResponseConverter.getMediaDetailAssetResponse(rawAssetsResponse)
        assertThat(mediaDetailAssetsResponse.assetType).isEqualTo(ContentType.AUDIO)
    }

    @Test
    fun `converter should identify mp4 or mov format like a video type`(){
        val rawAssetsResponse = getResponseFromJson(videoAssetJsonResponsePath)
        val mediaDetailAssetsResponse = assetsResponseConverter.getMediaDetailAssetResponse(rawAssetsResponse)
        assertThat(mediaDetailAssetsResponse.assetType).isEqualTo(ContentType.VIDEO)
    }

    @Test
    fun `converter should identify jpg format like a image type`(){
        val rawAssetsResponse = getResponseFromJson(imageAssetJsonResponsePath)
        val mediaDetailAssetsResponse = assetsResponseConverter.getMediaDetailAssetResponse(rawAssetsResponse)
        assertThat(mediaDetailAssetsResponse.assetType).isEqualTo(ContentType.IMAGE)
    }

    @Test
    fun `video asset can't contain jpg,jpeg,png and mp3`(){
        val rawAssetsResponse = getResponseFromJson(videoAssetJsonResponsePath)
        val mediaDetailAssetsResponse = assetsResponseConverter.getMediaDetailAssetResponse(rawAssetsResponse)
        val urlValues = mediaDetailAssetsResponse.assets.values.toString()
        val p = Pattern.compile("^((?!jpg|jpeg|png|mp3).)*\$")
        assertThat(urlValues).matches(p)
    }

    @Test
    fun `audio asset can't contain jpg,jpeg,png,mp4 and mov urls`(){
        val rawAssetsResponse = getResponseFromJson(audioAssetJsonResponsePath)
        val mediaDetailAssetsResponse = assetsResponseConverter.getMediaDetailAssetResponse(rawAssetsResponse)
        val urlValues = mediaDetailAssetsResponse.assets.values.toString()
        val p = Pattern.compile("^((?!jpg|jpeg|png|mp4|mov).)*\$")
        assertThat(urlValues).matches(p)
    }

    private fun getResponseFromJson(path:String): RawMediaDetailAssetResponse {
        val gson = Gson()
        return gson.fromJson(FileReader(path), RawMediaDetailAssetResponse::class.java)
    }

}