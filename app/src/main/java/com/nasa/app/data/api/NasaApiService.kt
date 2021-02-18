package com.nasa.app.data.api

import com.nasa.app.data.api.json.MediaDetailAssetResponse
import com.nasa.app.data.api.json.MediaDetailMetadataResponse
import com.nasa.app.data.api.json.MediaDetailResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface NasaApiService {
    @GET("search")
    fun mediaInfo(@Query("nasa_id")nasa_id:String): Single<MediaDetailResponse>

    @GET("asset/{nasa_id}")
    fun mediaAsset(@Path("nasa_id")nasa_id:String): Single<MediaDetailAssetResponse>

    @GET
    fun mediaMetadata(@Url url:String): Single<MediaDetailMetadataResponse>

}