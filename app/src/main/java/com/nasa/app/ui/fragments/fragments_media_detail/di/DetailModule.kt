package com.nasa.app.ui.fragments.fragments_media_detail.di

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.nasa.app.data.model.media_detail.MediaDetailResponse
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.fragments.di.FragmentScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named

//@InstallIn(FragmentComponent::class)
//@Module
//class DetailModule {
//
//    @FragmentScope
//    @Provides
//    fun providesCompositeDisposable(): CompositeDisposable {
//        return CompositeDisposable()
//    }
//
//    @Provides
//    fun provideExoPlayerInstance(context:Context): ExoPlayer{
//        return SimpleExoPlayer.Builder(context).build()
//    }
//
//    @FragmentScope
//    @Provides
//    fun providesDownloadMediaDetailMutableLiveData(): MutableLiveData<MediaDetailResponse> {
//       return MutableLiveData<MediaDetailResponse>()
//    }
//
//    @FragmentScope
//    @Named("media detail network state")
//    @Provides
//    fun provideDetailNetworkState(): MutableLiveData<NetworkState> {
//        return MutableLiveData<NetworkState>()
//    }
//
//
//}