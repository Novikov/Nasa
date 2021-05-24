package com.nasa.app.data.model.media_preview.raw_media_preview

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.nasa.app.data.model.ContentType
import org.junit.Test
import java.io.FileReader

class RawMediaPreviewResponseConverterTest {
    val longJsonResponsePath = "src/test/resources/media_preview/LongMediaPreviewResponse.json"
    val shortJsonResponsePath = "src/test/resources/media_preview/ShortMediaPreviewResponse.json"
    val singleImageJsonResponsePath = "src/test/resources/media_preview/SingleImageMediaPreviewResponse.json"
    val singleVideoJsonResponsePath = "src/test/resources/media_preview/SingleVideoMediaPreviewResponse.json"
    val singleAudioJsonResponsePath = "src/test/resources/media_preview/SingleAudioMediaPreviewResponse.json"
    val mediaPreviewResponseConverter = RawMediaPreviewResponseConverter()

    /**
     * 1.Converter should correct count totalPages
     * 2.Converter should cut description
     * 3.Converter should convert date?
     * 4.Converter should convert data type
     * */

    @Test
    fun `total pages property should be more 1 for total_hits more than 100`(){
        val rawMediaPreviewResponse = getResponseFromJson(longJsonResponsePath)
        val convertedResponse = mediaPreviewResponseConverter.getMediaPreviewResponse(rawMediaPreviewResponse)
        assertThat(convertedResponse.totalPages).isEqualTo(59)
    }

    @Test
    fun `total pages property should equals 1 for total_hits less than 100 `(){
        val rawMediaPreviewResponse = getResponseFromJson(shortJsonResponsePath)
        val convertedResponse = mediaPreviewResponseConverter.getMediaPreviewResponse(rawMediaPreviewResponse)
        assertThat(convertedResponse.totalPages).isEqualTo(1)
    }

    @Test
    fun `description property should be shortened to 200 characters if it greater`(){
        val rawMediaPreviewResponse = getResponseFromJson(singleImageJsonResponsePath)
        val convertedResponse = mediaPreviewResponseConverter.getMediaPreviewResponse(rawMediaPreviewResponse)
        assertThat(convertedResponse.mediaPreviewList[0].description.length).isEqualTo(200)
    }

    @Test
    fun `date property have to match the format (MMM d, yyyy)`(){
        val rawMediaPreviewResponse = getResponseFromJson(singleImageJsonResponsePath)
        val convertedResponse = mediaPreviewResponseConverter.getMediaPreviewResponse(rawMediaPreviewResponse)
        assertThat(convertedResponse.mediaPreviewList[0].dateCreated).isEqualTo("Apr 1, 2011")
    }

    @Test
    fun `images items should have ContentType enum type`(){
        val rawMediaPreviewResponse = getResponseFromJson(singleImageJsonResponsePath)
        val convertedResponse = mediaPreviewResponseConverter.getMediaPreviewResponse(rawMediaPreviewResponse)
        assertThat(convertedResponse.mediaPreviewList[0].mediaType).isEqualTo(ContentType.IMAGE)
    }

    @Test
    fun `video items should have ContentType enum type`(){
        val rawMediaPreviewResponse = getResponseFromJson(singleVideoJsonResponsePath)
        val convertedResponse = mediaPreviewResponseConverter.getMediaPreviewResponse(rawMediaPreviewResponse)
        assertThat(convertedResponse.mediaPreviewList[0].mediaType).isEqualTo(ContentType.VIDEO)
    }

    @Test
    fun `audio items should have ContentType enum type`(){
        val rawMediaPreviewResponse = getResponseFromJson(singleAudioJsonResponsePath)
        val convertedResponse = mediaPreviewResponseConverter.getMediaPreviewResponse(rawMediaPreviewResponse)
        assertThat(convertedResponse.mediaPreviewList[0].mediaType).isEqualTo(ContentType.AUDIO)
    }

    private fun getResponseFromJson(path:String): RawMediaPreviewResponse {
        val gson = Gson()
        return gson.fromJson(FileReader(path),RawMediaPreviewResponse::class.java)
    }
}