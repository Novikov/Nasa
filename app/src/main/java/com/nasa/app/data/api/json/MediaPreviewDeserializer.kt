package com.nasa.app.data.api.json

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.nasa.app.data.model.ContentType
import com.nasa.app.data.model.MediaPreview
import java.lang.reflect.Type
import java.util.ArrayList

class MediaPreviewDeserializer: JsonDeserializer<MediaPreviewResponse> {
    private val TAG: String = "MediaPreviewDeserialization"

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MediaPreviewResponse {
        val previewsList = ArrayList<MediaPreview>()

        var dateCreated: String = ""
        var previewUrl:String? = null
        var mediaType: String
        var nasaId: String = ""
        var description:String

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
                            if (it.asJsonObject.has("links")){
                                Log.i(TAG, "inside links array")

                                val links = it.asJsonObject.get("links")
                                val linksValue = links.asJsonArray.get(0).asJsonObject

                                linksValue.entrySet()?.forEach {
                                    if (it.key == "href"&&it.value.toString().contains("thumb")) {
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
                                        val tmpDateCreated = it.value.asString
                                        val endStringPosition = tmpDateCreated.indexOf('T')
                                        dateCreated = tmpDateCreated.subSequence(0,endStringPosition).toString()
                                    }

                                    if (it.key == "nasa_id") {
                                        Log.i(TAG, "nasa_is exists")
                                        nasaId = it.value.asString
                                    }


                                    if (it.key == "media_type") {
                                        Log.i(TAG, "media_type exists")
                                        mediaType = it.value.asString
                                    }

                                    if (it.key == "description") {
                                        Log.i(TAG, "description exists")
                                        description = it.value.asString
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        return MediaPreviewResponse(previewsList)
    }
}