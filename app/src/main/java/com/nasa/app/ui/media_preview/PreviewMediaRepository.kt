package com.nasa.app.ui.media_preview

import androidx.lifecycle.LiveData
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.api.json.MediaPreviewResponse
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.data.repository.PreviewsMediaDataSource
import com.nasa.app.ui.media_preview.di.PreviewScope
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@PreviewScope
class PreviewMediaRepository @Inject constructor(private val apiService: NasaApiService) {
    @Inject lateinit var previewMediaDataSource: PreviewsMediaDataSource

    fun fetchMultipleMediaPreview(): LiveData<MediaPreviewResponse> {
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