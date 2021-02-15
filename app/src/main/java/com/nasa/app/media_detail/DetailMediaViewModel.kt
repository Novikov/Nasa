package com.nasa.app.media_detail

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nasa.app.data.model.MediaDetail
import com.nasa.app.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class DetailMediaViewModel(private val mediaRepository : DetailMediaRepository, nasaId: String):ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val  mediaDetails : LiveData<MediaDetail> by lazy {
        mediaRepository.fetchSingleMediaDetail(compositeDisposable, nasaId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        mediaRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}