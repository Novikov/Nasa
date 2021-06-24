package com.nasa.app.ui.fragments.fragment_media_preview


import com.nasa.app.ui.fragments.fragment_media_preview.di.PreviewComponent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@EntryPoint
interface PreviewEntryPoint {
    fun previewComponent(): PreviewComponent.Factory
}