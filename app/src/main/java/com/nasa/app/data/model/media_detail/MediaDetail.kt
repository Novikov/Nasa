package com.nasa.app.data.model.media_detail

data class MediaDetail(
   val dateCreated: String,
   val nasaId: String,
   val previewUrl: String,
   val keywords: List<String>,
   val center: String,
   val title: String,
   val description: String,
   val assets: Map<String, String>?,
   val metadataUrl: String?,
   val fileSize: String?,
   val fileFormat: String?
)