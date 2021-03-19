package com.nasa.app.ui.media_detail.di

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class DetailModule {

    @DetailScope
    @Provides
    fun providesCompositeDisposable():CompositeDisposable{
        return CompositeDisposable()
    }
}