package com.nasa.app.data.api.json

import com.google.gson.annotations.SerializedName

class MediaDetailMetadataResponse(
    @SerializedName("File:FileSize")
    val fileSize:String,
    @SerializedName("XMP:Format")
    val fileFormat:String)