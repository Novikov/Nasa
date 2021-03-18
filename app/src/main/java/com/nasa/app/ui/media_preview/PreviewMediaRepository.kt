package com.nasa.app.ui.media_preview

import androidx.lifecycle.LiveData
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.api.json.MediaPreviewResponse
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.data.repository.PreviewsMediaDataSource
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PreviewMediaRepository @Inject constructor(private val apiService: NasaApiService) {
    lateinit var previewMediaDataSource: PreviewsMediaDataSource

    fun fetchMultipleMediaPreview(compositeDisposable: CompositeDisposable): LiveData<MediaPreviewResponse> {
        previewMediaDataSource = PreviewsMediaDataSource(apiService, compositeDisposable)
        previewMediaDataSource.fetchMediaPreviews()
        return previewMediaDataSource.downloadedMediaPreviewsResponse
    }

    fun updateMediaPreviews(){
        previewMediaDataSource.fetchMediaPreviews()
    }

    fun getMediaPreviewNetworkState(): LiveData<NetworkState> {
        return previewMediaDataSource.networkState
    }
}