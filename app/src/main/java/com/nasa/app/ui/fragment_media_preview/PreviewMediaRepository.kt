package com.nasa.app.ui.fragment_media_preview

import androidx.lifecycle.LiveData
import com.nasa.app.data.model.media_preview.MediaPreviewResponse
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.data.repository.PreviewsMediaDataSource
import com.nasa.app.ui.fragment_media_preview.di.PreviewScope
import javax.inject.Inject

@PreviewScope
class PreviewMediaRepository @Inject constructor(private val previewMediaDataSource: PreviewsMediaDataSource) {

    fun getMediaPreviews(): LiveData<MediaPreviewResponse> {
        previewMediaDataSource.getMediaPreviews()
        return previewMediaDataSource.downloadedMediaPreviewsResponse
    }

    fun updateMediaPreviews() {
        previewMediaDataSource.getMediaPreviews()
    }

    fun getMediaPreviewNetworkState(): LiveData<NetworkState> {
        return previewMediaDataSource.networkState
    }

    fun getInitialMediaPreviews(): LiveData<MediaPreviewResponse> {
        return previewMediaDataSource.initialDownloadedMediaPreviewsResponse
    }

    fun putInitialDataToMediaPreviews() {
        previewMediaDataSource.putInitialDataToDownloadedMediaResponse()
    }
}