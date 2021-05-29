package com.nasa.app.di

import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.api.NasaApiService
import dagger.Provides
import com.google.gson.GsonBuilder
import com.nasa.app.data.model.media_preview.MediaPreviewResponse
import com.nasa.app.utils.NASA_BASE_URL
import com.nasa.app.utils.SearchParams
import com.squareup.picasso.Picasso
import dagger.Module
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideNasaApiService() :NasaApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val gson = GsonBuilder()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(NASA_BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(NasaApiService::class.java)
    }

    @Singleton
    @Provides
    fun providePicassoInstance(): Picasso {
        return Picasso.get()
    }


    @Singleton
    @Named("initial search params")
    @Provides
    fun provideInitialSearchParams():SearchParams {
        return SearchParams()
    }
}