package com.nasa.app.data.model

data class MediaResponse (
    val mediaPreviewList : List<MediaPreview>,
    val totalResults:Int,
    val totalPages:Int
)