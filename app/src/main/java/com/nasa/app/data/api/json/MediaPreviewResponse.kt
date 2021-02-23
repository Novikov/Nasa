package com.nasa.app.data.api.json

import com.nasa.app.data.model.MediaPreview

class MediaPreviewResponse(val mediaPreviewList: List<MediaPreview>,
                           val page:Int,
                           val totalPages:Int,
                           val totalResults:Int)