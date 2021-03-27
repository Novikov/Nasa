package com.nasa.app.data.api

import com.nasa.app.data.model.media_detail.MediaDetailMetadataResponse
import com.nasa.app.data.model.media_detail.raw_media_asset.RawMediaDetailAssetResponse
import com.nasa.app.data.model.media_detail.raw_media_detail.RawMediaDetailResponse
import com.nasa.app.data.model.media_preview.raw_media_preview.RawMediaPreviewResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface NasaApiService {

    @GET("search")
    fun getMediaPreviews(
        @Query("q") q: String,
        @Query("media_type") mediaType: String,
        @Query("year_start") year_start: String,
        @Query("year_end") year_end: String,
        @Query("page")page:Int
    ): Single<RawMediaPreviewResponse>

    //MediaDetail
    @GET("search")
    fun getMediaDetailInfo(@Query("nasa_id") nasa_id: String): Single<RawMediaDetailResponse>

    @GET("asset/{nasa_id}")
    fun getMediaDetailAsset(@Path("nasa_id") nasa_id: String): Single<RawMediaDetailAssetResponse>

    @GET
    fun getMediaMetadata(@Url url: String): Single<MediaDetailMetadataResponse>


}