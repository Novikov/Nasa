package com.nasa.app.ui.media_preview

import androidx.lifecycle.LiveData
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.model.MediaDetail
import com.nasa.app.data.model.MediaPreview
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.data.repository.PreviewsMediaDataSource
import io.reactivex.disposables.CompositeDisposable

class PreviewMediaRepository(private val apiService: NasaApiService) {
    lateinit var previewMediaDataSource: PreviewsMediaDataSource

    fun fetchMultipleMediaPreview(compositeDisposable: CompositeDisposable): LiveData<List<MediaPreview>> {
        previewMediaDataSource = PreviewsMediaDataSource(apiService, compositeDisposable)
        previewMediaDataSource.fetchMediaPreviews()
        return previewMediaDataSource.downloadedMediaPreviewsResponse
    }

    fun getMediaPreviewNetworkState(): LiveData<NetworkState> {
        return previewMediaDataSource.networkState
    }
}