package com.nasa.app.data.api

import com.nasa.app.data.model.MediaDetail
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    @GET("search")
    fun mediaInfo(@Query("nasa_id")nasa_id:String): Single<MediaDetail>
}