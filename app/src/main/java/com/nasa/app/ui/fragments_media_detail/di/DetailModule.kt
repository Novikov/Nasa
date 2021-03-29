package com.nasa.app.ui.fragments_media_detail.di

import com.nasa.app.utils.ExoMediaPlayer
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
    fun provideExoMediaPlayer(): ExoMediaPlayer {
        return ExoMediaPlayer()
    }
}