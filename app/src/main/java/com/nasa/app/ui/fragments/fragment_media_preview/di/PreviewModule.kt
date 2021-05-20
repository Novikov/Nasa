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
    @Provides
    fun providesCompositeDisposable():CompositeDisposable{
        return CompositeDisposable()
    }

    @FragmentScope
    @Named("media previews")
    @Provides
    fun provideMediaPreviewResponseMutableLiveData(): MutableLiveData<MediaPreviewResponse> {
        return MutableLiveData<MediaPreviewResponse>()
    }



    @FragmentScope
    @Named("media previews network state")
    @Provides
    fun providesMediaPreviewNetworkState(): MutableLiveData<NetworkState> {
        return MutableLiveData<NetworkState>()
    }

}