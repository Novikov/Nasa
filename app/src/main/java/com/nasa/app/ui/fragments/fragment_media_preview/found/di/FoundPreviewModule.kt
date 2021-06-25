package com.nasa.app.ui.fragments.fragment_media_preview.found.di

import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.repository.NetworkState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.disposables.CompositeDisposable

@InstallIn(ViewModelComponent::class)
@Module
class FoundPreviewModule {
    @ViewModelScoped
    @FoundMediaPreviewsCompositeDisposable
    @Provides
    fun providesFoundCompositeDisposable():CompositeDisposable{
        return CompositeDisposable()
    }

    @ViewModelScoped
    @FoundMediaPreviewsNetworkState
    @Provides
    fun providesFoundMediaPreviewNetworkState(): MutableLiveData<NetworkState> {
        return MutableLiveData<NetworkState>()
    }
}