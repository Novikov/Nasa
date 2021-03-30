package com.nasa.app.ui.fragments_media_detail.di

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class DetailModule {

    @Provides
    fun provideNasaId(nasaId: String): String {
        return nasaId
    }

    @DetailScope
    @Provides
    fun providesCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    fun provideExoPlayerInstance(context:Context): ExoPlayer{
        return SimpleExoPlayer.Builder(context).build()
    }
}