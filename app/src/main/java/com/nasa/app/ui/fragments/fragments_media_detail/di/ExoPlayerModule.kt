package com.nasa.app.ui.fragments.fragments_media_detail.di

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(FragmentComponent::class)
@Module
class ExoPlayerModule {
    @Provides
    fun provideExoPlayerInstance(@ApplicationContext context: Context): ExoPlayer {
        return SimpleExoPlayer.Builder(context).build()
    }
}