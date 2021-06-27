package com.nasa.app.ui.fragments.fragments_media_detail.di

import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.repository.NetworkState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named

@InstallIn(FragmentComponent::class)
@Module
class DetailModule {

    @FragmentScoped
    @Named("Media Details Composite Disposable")
    @Provides
    fun providesCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @FragmentScoped
    @Named("Media Details Network State")
    @Provides
    fun provideDetailNetworkState(): MutableLiveData<NetworkState> {
        return MutableLiveData<NetworkState>()
    }
}