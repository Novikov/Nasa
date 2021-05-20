package com.nasa.app.data.model.media_preview.raw_media_preview

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import org.junit.Test
import java.io.FileReader

class RawMediaPreviewResponseConverterTest {
    val jsonPath = "/home/x/GIT/Nasa/app/src/test/java/com/nasa/app/data/model/media_preview/raw_media_preview/RawMediaPreviewResponse.json"
    val gson = Gson()

    @Test
    fun `media previews response converter should convert images`(){
        val rawMediaPreviewResponse = gson.fromJson(FileReader(jsonPath),RawMediaPreviewResponse::class.java)
        assertThat(rawMediaPreviewResponse.collection.items.size).isEqualTo(1)
    }
}