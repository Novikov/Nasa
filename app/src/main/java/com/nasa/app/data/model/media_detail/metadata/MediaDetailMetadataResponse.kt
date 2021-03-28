package com.nasa.app.data.model.media_detail.metadata

import com.google.gson.annotations.SerializedName

class MediaDetailMetadataResponse(
    @SerializedName("File:FileSize")
    val fileSize: String,
    @SerializedName("File:FileType")
    val fileFormat: String
)