package com.nasa.app.data.model.media_detail.raw_media_detail

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.nasa.app.data.model.media_preview.raw_media_preview.RawMediaPreviewResponse
import com.nasa.app.data.model.media_preview.raw_media_preview.RawMediaPreviewResponseConverter
import org.junit.Test

import java.io.FileReader

class RawMediaDetailResponseConverterTest {
    val videoDetailJsonResponsePath = "src/test/resources/media_detail/VideoDetail.json"
    val imageDetailJsonResponsePath = "src/test/resources/media_detail/ImageDetail.json"
    val audioDetailJsonResponsePath = "src/test/resources/media_detail/AudioDetail.json"
    val mediaDetailResponseConverter = RawMediaDetailResponseConverter()

    @Test
    fun `converter should convert date`(){
        val rawMediaDetailResponse = getResponseFromJson(videoDetailJsonResponsePath)
        val convertedResponse = mediaDetailResponseConverter.getMediaDetailResponseWithInfoData(rawMediaDetailResponse)
        assertThat(convertedResponse.item.dateCreated).isEqualTo("16-07-2014")
    }

    private fun getResponseFromJson(path:String): RawMediaDetailResponse {
        val gson = Gson()
        return gson.fromJson(FileReader(path), RawMediaDetailResponse::class.java)
    }
}