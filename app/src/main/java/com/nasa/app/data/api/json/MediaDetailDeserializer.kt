package com.nasa.app.data.api.json

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.nasa.app.data.model.media_detail.MediaDetail
import com.nasa.app.data.model.media_detail.MediaDetailResponse
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class MediaDetailDeserializer : JsonDeserializer<MediaDetailResponse> {
    private val TAG: String = "MediaDetailDeserialization"

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MediaDetailResponse {

        var dateCreated = ""
        var nasaId = ""
        var previewUrl = ""
        var center = ""
        var title = ""
        var description = ""
        var location = ""
        var keywordList = mutableListOf("")

        json?.asJsonObject?.entrySet()?.forEach {
            Log.i(TAG, "Deserialization of MediaDetail begins")

            if (it.key.equals("collection")) {
                Log.i(TAG, "inside collection object")

                it.value.asJsonObject.entrySet()?.forEach {
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

                                        val monthDateFormat = SimpleDateFormat("MM")
                                        val month = monthDateFormat.format(parsedDate)

                                        val yearDateFormat = SimpleDateFormat("yyyy")
                                        val year = yearDateFormat.format(parsedDate)

                                        dateCreated = "$day-$month-$year"
                                    }

                                    if (it.key == "nasa_id") {
                                        Log.i(TAG, "nasa_is exists")
                                        nasaId = it.value.asString
                                    }

                                    if (it.key == "keywords") {
                                        Log.i(TAG, "keywords exists")
                                        keywordList = mutableListOf()
                                        it.value.asJsonArray.forEach {
                                            keywordList!!.add(it.asString)
                                        }
                                    }

                                    if (it.key == "center") {
                                        Log.i(TAG, "center exists")
                                        center = it.value.asString
                                    }

                                    if (it.key == "title") {
                                        Log.i(TAG, "title exists")
                                        title = it.value.asString
                                    }

                                    if (it.key == "description") {
                                        Log.i(TAG, "description exists")
                                        description = it.value.asString
                                    }

                                    if (it.key == "location") {
                                        Log.i(TAG, "location exists")
                                        location = it.value.asString
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        val mediaDetail = MediaDetail(
            dateCreated,
            nasaId,
            previewUrl,
            keywordList,
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
}