package com.nasa.app.ui.fragments.fragments_media_detail

import androidx.lifecycle.LiveData
import com.nasa.app.data.model.media_detail.MediaDetailResponse
import com.nasa.app.data.repository.DetailMediaDataSource
import com.nasa.app.data.repository.NetworkState
import javax.inject.Inject

class DetailMediaRepository @Inject constructor(private val detailMediaDataSource: DetailMediaDataSource) {

    fun getMediaDetail(nasaId: String): LiveData<MediaDetailResponse> {
        detailMediaDataSource.getMediaDetail(nasaId)
        return detailMediaDataSource.downloadedMediaResponse
    }

    fun getMediaDetailsNetworkState(): LiveData<NetworkState> {
        return detailMediaDataSource.networkState
    }

}