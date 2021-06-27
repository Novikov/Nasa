package com.nasa.app.data.model.media_detail.raw_media_detail

import android.util.Log
import com.nasa.app.data.model.media_detail.MediaDetail
import com.nasa.app.data.model.media_detail.MediaDetailResponse
import com.nasa.app.utils.EMPTY_STRING
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RawMediaDetailResponseConverter @Inject constructor() {

    lateinit var dateCreated: String
    lateinit var nasaId: String
    lateinit var previewUrl: String
    lateinit var keywords: List<String>
    lateinit var center: String
    lateinit var title: String
    lateinit var description: String
    val previewAssetSubstring = "preview"

    fun getMediaDetailResponseWithInfoData(rawMediaDetailResponse: RawMediaDetailResponse): MediaDetailResponse {
        val tmpNasaId: String? =
            rawMediaDetailResponse.collection.items.first().data.first().nasa_id
        nasaId = tmpNasaId ?: EMPTY_STRING

        val tmpDescription: String? =
            rawMediaDetailResponse.collection.items.first().data.first().description
        description = tmpDescription ?: EMPTY_STRING

        val tmpTitle: String? = rawMediaDetailResponse.collection.items.first().data.first().title
        title = tmpTitle ?: EMPTY_STRING

        val tmpDateCreated =
            rawMediaDetailResponse.collection.items.first().data.first().date_created
        dateCreated = convertDate(tmpDateCreated)

        val tmpCenter: String? = rawMediaDetailResponse.collection.items.first().data.first().center
        center = tmpCenter ?: EMPTY_STRING

        val tmpKeywords: List<String>? =
            rawMediaDetailResponse.collection.items.first().data.first().keywords
        keywords = tmpKeywords ?: listOf(EMPTY_STRING)

        //preview url for audio and video items is always null
        val tmpLinks: List<AssetLink>? = rawMediaDetailResponse.collection.items.first().links
        if (tmpLinks != null) {
            rawMediaDetailResponse.collection.items.first().links.forEach {
                if (it.rel.equals(previewAssetSubstring)) {
                    previewUrl = it.href
                }
            }
        } else {
            previewUrl = EMPTY_STRING
        }

        val mediaDetail = MediaDetail(
            dateCreated,
            nasaId,
            previewUrl,
            keywords,
            center,
            title,
            description,
            null,
            null,
            null,
            null
        )

        return MediaDetailResponse(mediaDetail)
    }

    private fun convertDate(dateStr: String): String {
        if (dateStr.equals(EMPTY_STRING)) {
            return EMPTY_STRING
        }

        val parsedDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val targetFormat = SimpleDateFormat("dd-MM-yyyy")

        var parsedDate: Date? = null

        try {
            parsedDate = parsedDateFormat.parse(dateStr)
        }
        catch (ex: ParseException){
            Log.i(TAG, "convertDate: parsing error")
        }

        return if (parsedDate != null) {
            val dateCreated = targetFormat.format(parsedDate)
            dateCreated.toString()
        } else {
            EMPTY_STRING
        }
    }

    companion object {
        private const val TAG = "RawMediaDetailResponseC"
    }
}