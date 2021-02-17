package com.nasa.app.data.api

import com.google.gson.GsonBuilder
import com.nasa.app.data.api.json.MediaAssetDeserializer
import com.nasa.app.data.api.json.MediaAssetResponse
import com.nasa.app.data.api.json.MediaDetailDeserializer
import com.nasa.app.data.api.json.MediaDetailResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NasaApiClient {
    private const val NASA_BASE_URL="https://images-api.nasa.gov/"

    fun getClient(): NasaApiService {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client =  OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(
                MediaDetailResponse::class.java,
                MediaDetailDeserializer()
            )
            .registerTypeAdapter(
                MediaAssetResponse::class.java,
                MediaAssetDeserializer()
            )
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(NASA_BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(NasaApiService::class.java)
    }
}