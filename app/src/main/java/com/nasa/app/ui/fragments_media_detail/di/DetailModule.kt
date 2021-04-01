package com.nasa.app.ui.fragments_media_detail.di

import android.content.Context
import android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT
import android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class DetailModule {

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