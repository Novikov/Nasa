package com.nasa.app.data.model.media_detail.raw_media_detail

import android.util.Log
import com.nasa.app.data.model.media_detail.MediaDetail
import com.nasa.app.data.model.media_detail.MediaDetailResponse
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RawMediaDetailResponseConverter @Inject constructor(){

    lateinit var dateCreated: String
    lateinit var nasaId: String
    lateinit var previewUrl: String
    lateinit var keywords: List<String>
    lateinit var center: String
    lateinit var title: String
    lateinit var description: String

    fun getMediaDetailResponseWithInfoData(rawMediaDetailResponse: RawMediaDetailResponse): MediaDetailResponse {
        nasaId = rawMediaDetailResponse.collection.items.first().data.first().nasa_id

        val tmpDescription = rawMediaDetailResponse.collection.items.first().data.first().description
        description = if (tmpDescription!=null){
            tmpDescription
        } else{
            ""
        }

        title = rawMediaDetailResponse.collection.items.first().data.first().title
        val tmpDateCreated = rawMediaDetailResponse.collection.items.first().data.first().date_created
        dateCreated = convertDate(tmpDateCreated)
        center = rawMediaDetailResponse.collection.items.first().data.first().center
        keywords = rawMediaDetailResponse.collection.items.first().data.first().keywords

        //preview url for audio and video items is always null
        if (rawMediaDetailResponse.collection.items.first().links!=null){
            rawMediaDetailResponse.collection.items.first().links.forEach {
                if (it.rel.equals("preview")){
                    previewUrl = it.href
                }
            }
        }
        else {
            previewUrl = ""
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

    fun convertDate (dateStr: String): String {
        val parsedDateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")

        val parsedDate: Date = parsedDateFormat.parse(dateStr)

        val dayDateFormat = SimpleDateFormat("dd")
        val day = dayDateFormat.format(parsedDate)

        val monthDateFormat = SimpleDateFormat("MM")
        val month = monthDateFormat.format(parsedDate)

        val yearDateFormat = SimpleDateFormat("yyyy")
        val year = yearDateFormat.format(parsedDate)

        val convertedDate = "$day-$month-$year"

        return convertedDate
    }
}