package com.nasa.app.ui.media_preview.di

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class PreviewModule {

    @PreviewScope
    @Provides
    fun providesCompositeDisposable():CompositeDisposable{
        return CompositeDisposable()
    }
}