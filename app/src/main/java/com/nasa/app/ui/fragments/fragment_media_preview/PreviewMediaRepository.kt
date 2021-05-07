package com.nasa.app.ui.fragments.fragment_media_preview

import androidx.lifecycle.LiveData
import com.nasa.app.data.model.media_preview.MediaPreviewResponse
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.data.repository.PreviewsMediaDataSource
import com.nasa.app.ui.fragments.di.FragmentScope
import javax.inject.Inject

@FragmentScope
class PreviewMediaRepository @Inject constructor(private val previewMediaDataSource: PreviewsMediaDataSource) {

    fun getMediaPreviews(): LiveData<MediaPreviewResponse> {
        previewMediaDataSource.getMediaPreviews()
        return previewMediaDataSource.downloadedMediaPreviewsResponse
    }

    fun getInitialMediaPreviews(): LiveData<MediaPreviewResponse> {
        return previewMediaDataSource.initialDownloadedMediaPreviewsResponse
    }

    fun updateMediaPreviews() {
        previewMediaDataSource.getMediaPreviews()
    }

    fun getMediaPreviewNetworkState(): LiveData<NetworkState> {
        return previewMediaDataSource.networkState
    }

    fun putInitialDataToMediaPreviews() {
        previewMediaDataSource.putInitialDataToDownloadedMediaResponse()
    }
}