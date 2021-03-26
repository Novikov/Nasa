package com.nasa.app.data.model.media_preview

data class Collection(
    val href: String,
    val items: List<Item>,
    val links: List<PageLink>,
    val metadata: Metadata,
    val version: String
)