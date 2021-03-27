package com.nasa.app.data.model.media_preview.raw_data

data class Collection(
    val href: String,
    val items: List<Item>,
    val metadata: Metadata,
)