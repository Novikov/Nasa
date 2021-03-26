package com.nasa.app.data.model.media_preview

data class Item(
    val data: List<Data>,
    val href: String,
    val links: List<Link>
)