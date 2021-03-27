package com.nasa.app.data.api

import com.nasa.app.data.model.media_detail.MediaDetailAssetResponse
import com.nasa.app.data.model.media_detail.MediaDetailMetadataResponse
import com.nasa.app.data.model.media_detail.MediaDetailResponse
import com.nasa.app.data.model.media_detail.raw_media_detail.RawMediaDetailResponse
import com.nasa.app.data.model.media_preview.raw_data.RawMediaPreviewResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface NasaApiService {
    @GET("search")
    fun mediaPreview(
        @Query("q") q: String,
        @Query("media_type") mediaType: String,
        @Query("year_start") year_start: String,
        @Query("year_end") year_end: String,
        @Query("page")page:Int
    ): Single<RawMediaPreviewResponse>


    //MediaDetail
    @GET("search")
    fun mediaInfo(@Query("nasa_id") nasa_id: String): Single<MediaDetailResponse>@GET("search")

    fun mediaInfo2(@Query("nasa_id") nasa_id: String): Single<RawMediaDetailResponse>

    @GET("asset/{nasa_id}")
    fun mediaAsset(@Path("nasa_id") nasa_id: String): Single<MediaDetailAssetResponse>

    @GET
    fun mediaMetadata(@Url url: String): Single<MediaDetailMetadataResponse>


}