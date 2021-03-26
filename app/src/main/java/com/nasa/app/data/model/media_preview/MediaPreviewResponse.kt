package com.nasa.app.data.model.media_preview

data class MediaPreviewResponse(val mediaPreviewList: List<MediaPreview>,
                           val page:Int,
                           val totalPages:Int,
                           val totalResults:Int)