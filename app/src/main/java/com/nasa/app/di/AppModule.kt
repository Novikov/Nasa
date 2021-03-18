package com.nasa.app.di

import com.nasa.app.data.api.NasaApiService
import dagger.Provides
import com.google.gson.GsonBuilder
import com.nasa.app.NASA_BASE_URL
import com.nasa.app.data.api.json.*
import com.squareup.picasso.Picasso
import dagger.Module
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class AppModule {

    @Provides
    fun provideNasaApiService() :NasaApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(
                MediaDetailResponse::class.java,
                MediaDetailDeserializer()
            )
            .registerTypeAdapter(
                MediaDetailAssetResponse::class.java,
                MediaAssetDeserializer()
            )
            .registerTypeAdapter(
                MediaPreviewResponse::class.java,
                MediaPreviewDeserializer()
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

    @Provides
    fun providePicassoInstance(): Picasso {
        return Picasso.get()
    }
}