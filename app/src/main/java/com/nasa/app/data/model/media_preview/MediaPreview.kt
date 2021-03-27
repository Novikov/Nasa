package com.nasa.app.data.model.media_preview

import com.nasa.app.data.model.ContentType

data class MediaPreview(
    val nasaId: String,
    val previewUrl: String,
    val mediaType: ContentType,
    val dateCreated: String,
    val description: String
)