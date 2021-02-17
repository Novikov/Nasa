package com.nasa.app.data.model

data class MediaDetail(
   val dateCreated:String,
   val nasaId:String,
   val keywords:List<String>,
   val mediaType:String,
   val center:String,
   val title:String,
   val description:String,
   val location:String,
   var assets:Map<String,String>?
)