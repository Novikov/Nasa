package com.nasa.app.data.model

data class MediaDetail(
   val date_created:String,
   val nasa_id:String,
   val keywords:List<String>,
   val media_type:String,
   val center:String,
   val title:String,
   val description:String,
   val location:String
)