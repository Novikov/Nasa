package com.nasa.app.data.model.media_preview.raw_media_preview

import java.util.*

data class Collection(
    val href: String,
    val items: LinkedList<Item>,
    val metadata: Metadata,
)