package com.nasa.app.data.api.json

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.nasa.app.data.model.MediaDetail
import java.lang.reflect.Type

class MediaDetailDeserializer : JsonDeserializer<MediaDetailResponse> {
    private val TAG: String = "MediaDetailDeserialization"

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MediaDetailResponse {

        var dateCreated: String = ""
        var nasaId: String = ""
        var mediaType: String = ""
        var center: String = ""
        var title: String = ""
        var description: String = ""
        var location: String = ""
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
                            if (it.asJsonObject.has("data")) {
                                Log.i(TAG, "inside data array")

                                val data = it.asJsonObject.get("data")
                                val dataValue = data.asJsonArray.get(0).asJsonObject

                                dataValue.entrySet()?.forEach {
                                    if (it.key == "date_created") {
                                        Log.i(TAG, "date_created exists")
                                        dateCreated = it.value.asString
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

                                    if (it.key == "media_type") {
                                        Log.i(TAG, "media_type exists")
                                        mediaType = it.value.asString
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
            keywordList,
            mediaType,
            center,
            title,
            description,
            location
        )

        return MediaDetailResponse(mediaDetail)
    }

}