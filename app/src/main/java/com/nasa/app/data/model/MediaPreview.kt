package com.nasa.app.data.model

data class MediaPreview(
    val nasaId: String,
    val previewUrl: String,
    val mediaType: ContentType,
    val dateCreated: String,
    val description: String
)