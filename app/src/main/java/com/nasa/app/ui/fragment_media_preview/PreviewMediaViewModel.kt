package com.nasa.app.ui.fragment_media_preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nasa.app.data.model.media_preview.MediaPreviewResponse
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.fragment_media_preview.di.PreviewScope
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@PreviewScope
class PreviewMediaViewModel @Inject constructor(
    private val mediaRepository: PreviewMediaRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    val mediaPreviews: LiveData<MediaPreviewResponse> by lazy {
        mediaRepository.getMediaPreviews()
    }

    val initialMediaPreviews : LiveData<MediaPreviewResponse> by lazy {
        mediaRepository.getInitialMediaPreviews()
    }

    val networkState: LiveData<NetworkState> by lazy {
        mediaRepository.getMediaPreviewNetworkState()
    }

    fun updateMediaPreviews(){
        mediaRepository.updateMediaPreviews()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun putInitialDataToMediaPreviews() {
        mediaRepository.putInitialDataToMediaPreviews()
    }
}