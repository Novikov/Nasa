package com.nasa.app.data.api.json

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.MediaPreview
import com.nasa.app.ui.POST_PER_PAGE
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class MediaPreviewDeserializer : JsonDeserializer<MediaPreviewResponse> {
    private val TAG: String = "MediaPreviewDeserialization"

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MediaPreviewResponse {
        val previewsList = ArrayList<MediaPreview>()
        var dateCreated = ""
        var previewUrl = ""
        var mediaType: ContentType = ContentType.UNKNOWN
        var nasaId = ""
        var description = ""
        var totalResults = 0
        var page = 1
        var totalPages = 1
        json?.asJsonObject?.entrySet()?.forEach {
            Log.i(TAG, "Deserialization of MediaDetail begins")

            if (it.key.equals("collection")) {
                Log.i(TAG, "inside collection object")

                it.value.asJsonObject.entrySet()?.forEach {
                    if(it.key.equals("metadata")){
                        val metadata = it.value.asJsonObject
                        totalResults = metadata.get("total_hits").asInt
                        Log.i(TAG, "total results: $totalResults")
                    }

                    if (it.key.equals("items")) {
                        Log.i(TAG, "inside items array")

                        val items = it.value.asJsonArray
                        items.forEach {

                            //getting preview url
                            if (it.asJsonObject.has("links")) {
                                Log.i(TAG, "inside links array")

                                val links = it.asJsonObject.get("links")
                                val linksValue = links.asJsonArray.get(0).asJsonObject

                                linksValue.entrySet()?.forEach {
                                    if (it.key == "href" && it.value.toString().contains("thumb")) {
                                        Log.i(TAG, "preview url exists")
                                        previewUrl = it.value.asString
                                    }
                                }
                            }

                            //getting dateCreated,nasaId,mediaTYpe and description
                            if (it.asJsonObject.has("data")) {
                                Log.i(TAG, "inside data array")

                                val data = it.asJsonObject.get("data")
                                val dataValue = data.asJsonArray.get(0).asJsonObject

                                dataValue.entrySet()?.forEach {

                                    if (it.key == "date_created") {
                                        Log.i(TAG, "date_created exists")

                                        val dateStr: String = it.value.asString
                                        val parsedDateFormat =
                                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")

                                        val parsedDate: Date = parsedDateFormat.parse(dateStr)

                                        val dayDateFormat = SimpleDateFormat("dd")
                                        val day = dayDateFormat.format(parsedDate)

                                        val monthDateFormat = SimpleDateFormat("MMMM")
                                        val month = monthDateFormat.format(parsedDate)

                                        val yearDateFormat = SimpleDateFormat("yyyy")
                                        val year = yearDateFormat.format(parsedDate)

                                        dateCreated = "$month $day, $year"
                                    }

                                    if (it.key == "nasa_id") {
                                        Log.i(TAG, "nasa_is exists")
                                        nasaId = it.value.asString
                                    }


                                    if (it.key == "media_type") {
                                        Log.i(TAG, "media_type exists")
                                        val tmpMediaType = it.value.asString
                                        when (tmpMediaType) {
                                            "image" -> {
                                                mediaType = ContentType.IMAGE
                                            }
                                            "audio" -> {
                                                mediaType = ContentType.AUDIO
                                            }
                                            "video" -> {
                                                mediaType = ContentType.VIDEO
                                            }
                                        }
                                    }

                                    if (it.key == "description") {
                                        Log.i(TAG, "description exists")
                                        val tmpDescription = it.value.asString
                                        if (tmpDescription.length > 200) {
                                            description = tmpDescription.substring(0, 200)
                                        } else {
                                            description = it.value.asString
                                        }
                                    }
                                }
                            }

                            if (mediaType != ContentType.AUDIO) {
                                if (previewUrl.isNotEmpty()) {
                                    previewsList.add(
                                        MediaPreview(
                                            nasaId,
                                            previewUrl,
                                            mediaType,
                                            dateCreated,
                                            description
                                        )
                                    )
                                }
                            } else {
                                previewsList.add(
                                    MediaPreview(
                                        nasaId,
                                        previewUrl,
                                        mediaType,
                                        dateCreated,
                                        description
                                    )
                                )
                            }

                            //variables clearing for next iteration
                            dateCreated = ""
                            previewUrl = ""
                            mediaType = ContentType.UNKNOWN
                            nasaId = ""
                            description = ""
                        }
                    }

                    if (it.key.equals("href")){
                        page = it.value.asString.substringAfter("page=").toInt()
                        Log.i(TAG, "current page: $page")
                    }
                }
            }
        }

        Log.i("DeserializationResult", previewsList.toString())

        totalPages = if ((totalResults % POST_PER_PAGE)!=0){
            (totalResults/ POST_PER_PAGE) + 1
        } else {
            (totalResults/ POST_PER_PAGE)
        }

        Log.i(TAG, "totalPages: $totalPages")

        return MediaPreviewResponse(previewsList,page,totalPages,totalResults)
    }
}