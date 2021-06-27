package com.nasa.app.ui.fragments.fragments_media_detail.di

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.BasePlayer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.nasa.app.data.model.media_detail.MediaDetailResponse
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.fragments.di.FragmentScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
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