package com.nasa.app.ui.fragments.fragments_media_detail

import com.nasa.app.ui.fragments.fragments_media_detail.di.DetailComponent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@EntryPoint
interface DetailEntryPoint {
    fun detailComponent():DetailComponent.Factory
}