package com.nasa.app.ui.media_preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nasa.app.data.api.json.MediaPreviewResponse
import com.nasa.app.data.model.MediaPreview
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.media_detail.DetailMediaRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PreviewMediaViewModel @Inject constructor(private val mediaRepository: PreviewMediaRepository, private val compositeDisposable: CompositeDisposable) : ViewModel() {

    val mediaPreviews: LiveData<MediaPreviewResponse> by lazy {
        mediaRepository.fetchMultipleMediaPreview()
    }

    val networkState: LiveData<NetworkState> by lazy {
        mediaRepository.getMediaPreviewNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}