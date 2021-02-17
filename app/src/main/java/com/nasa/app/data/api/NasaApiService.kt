package com.nasa.app.data.api

import com.nasa.app.data.api.json.MediaAssetResponse
import com.nasa.app.data.api.json.MediaDetailResponse
import com.nasa.app.data.model.MediaDetail
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NasaApiService {
    @GET("search")
    fun mediaInfo(@Query("nasa_id")nasa_id:String): Single<MediaDetailResponse>

    @GET("asset/{nasa_id}")
    fun mediaAsset(@Path("nasa_id")nasa_id:String): Single<MediaAssetResponse>

}