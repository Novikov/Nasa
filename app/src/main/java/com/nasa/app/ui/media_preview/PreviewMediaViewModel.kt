package com.nasa.app.ui.media_preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nasa.app.data.model.MediaPreview
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.SEARCH_REQUEST_QUERY
import com.nasa.app.ui.media_detail.DetailMediaRepository
import io.reactivex.disposables.CompositeDisposable

class PreviewMediaViewModel(private val mediaRepository : PreviewMediaRepository):ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val mediaPreviews:LiveData<List<MediaPreview>> by lazy {
        mediaRepository.fetchMultipleMediaPreview(compositeDisposable)
    }

    val networkState : LiveData<NetworkState> by lazy {
        mediaRepository.getMediaPreviewNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}