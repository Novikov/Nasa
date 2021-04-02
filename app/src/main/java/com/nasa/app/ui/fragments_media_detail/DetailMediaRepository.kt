package com.nasa.app.ui.fragments_media_detail

import androidx.lifecycle.LiveData
import com.nasa.app.data.model.media_detail.MediaDetailResponse
import com.nasa.app.data.repository.DetailMediaDataSource
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.fragments_media_detail.di.DetailScope
import javax.inject.Inject

@DetailScope
class DetailMediaRepository @Inject constructor(private val detailMediaDataSource: DetailMediaDataSource) {

    fun getMediaDetail(
    ): LiveData<MediaDetailResponse> {
        detailMediaDataSource.getMediaDetail()
        return detailMediaDataSource.downloadedMediaResponse
    }

    fun getMediaDetailsNetworkState(): LiveData<NetworkState> {
        return detailMediaDataSource.networkState
    }

}