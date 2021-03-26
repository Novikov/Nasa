package com.nasa.app.data.model.media_preview

import com.google.gson.annotations.SerializedName

data class Item(
    val data: List<Data>,
    val href: String, //collection json url
    val links: List<AssetLink>
)