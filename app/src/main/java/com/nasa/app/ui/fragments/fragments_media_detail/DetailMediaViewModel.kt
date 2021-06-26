package com.nasa.app.ui.fragments.fragments_media_detail

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nasa.app.data.model.media_detail.MediaDetailResponse
import com.nasa.app.data.repository.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class DetailMediaViewModel @Inject constructor(
    private val mediaRepository: DetailMediaRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel(){

    val mediaDetails: LiveData<MediaDetailResponse> by lazy {
        mediaRepository.getMediaDetail()
    }

    val networkState: LiveData<NetworkState> by lazy {
        mediaRepository.getMediaDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}