package com.nasa.app.data.model

data class MediaDetail(
    val nasa_id:String,
    val title:String,
    val description:String?,
    val keywords:List<String>?,
    val media_type:String,
    val center:String?,
    val date_created:String,
    val preview_url:String?,
    val all_media_url:String,
    val playing_media_url:String
)