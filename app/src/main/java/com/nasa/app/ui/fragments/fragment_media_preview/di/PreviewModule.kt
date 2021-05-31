package com.nasa.app.ui.fragments.fragment_media_preview.di

import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.model.media_preview.MediaPreviewResponse
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.fragments.di.FragmentScope
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named

@Module
class PreviewModule {

    @FragmentScope
    @Named("initial media previews composite disposable")
    @Provides
    fun providesInitialCompositeDisposable():CompositeDisposable{
        return CompositeDisposable()
    }
    @FragmentScope
    @Named("found media previews composite disposable")
    @Provides
    fun providesFoundCompositeDisposable():CompositeDisposable{
        return CompositeDisposable()
    }

    @FragmentScope
    @Named("initial media previews network state")
    @Provides
    fun providesInitialMediaPreviewNetworkState(): MutableLiveData<NetworkState> {
        return MutableLiveData<NetworkState>()
    }

    @FragmentScope
    @Named("found media previews network state")
    @Provides
    fun providesFoundMediaPreviewNetworkState(): MutableLiveData<NetworkState> {
        return MutableLiveData<NetworkState>()
    }
}