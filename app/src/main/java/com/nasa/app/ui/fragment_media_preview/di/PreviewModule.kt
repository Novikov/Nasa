package com.nasa.app.ui.fragment_media_preview.di

import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.model.media_preview.MediaPreviewResponse
import com.nasa.app.data.repository.NetworkState
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named

@Module
class PreviewModule {

    @PreviewScope
    @Provides
    fun providesCompositeDisposable():CompositeDisposable{
        return CompositeDisposable()
    }

    @PreviewScope
    @Named("media previews")
    @Provides
    fun provideMediaPreviewResponseMutableLiveData(): MutableLiveData<MediaPreviewResponse> {
        return MutableLiveData<MediaPreviewResponse>()
    }



    @PreviewScope
    @Named("media previews network state")
    @Provides
    fun providesMediaPreviewNetworkState(): MutableLiveData<NetworkState> {
        return MutableLiveData<NetworkState>()
    }

}