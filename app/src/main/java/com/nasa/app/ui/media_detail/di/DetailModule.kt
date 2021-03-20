package com.nasa.app.ui.media_detail.di

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class DetailModule() {

    @Provides
    fun provideNasaId(nasaId:String): String {
        return nasaId
    }

    @DetailScope
    @Provides
    fun providesCompositeDisposable():CompositeDisposable{
        return CompositeDisposable()
    }
}