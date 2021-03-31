package com.nasa.app.data.model.media_detail.raw_media_detail

data class Item(
    val data: List<Data>,
    val href: String,
    val links: List<AssetLink>
)