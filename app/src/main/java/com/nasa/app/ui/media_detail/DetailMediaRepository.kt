package com.nasa.app.ui.media_detail

import androidx.lifecycle.LiveData
import com.nasa.app.data.model.media_detail.MediaDetail
import com.nasa.app.data.repository.DetailMediaDataSource
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.media_detail.di.DetailScope
import javax.inject.Inject

@DetailScope
class DetailMediaRepository @Inject constructor() {
   @Inject lateinit var detailMediaDataSource: DetailMediaDataSource

    fun fetchSingleMediaDetail(
    ): LiveData<MediaDetail> {
        detailMediaDataSource.fetchMediaDetails()
        detailMediaDataSource.fetchMediaDetails2()
        return detailMediaDataSource.downloadedMediaResponse
    }

    fun getMediaDetailsNetworkState(): LiveData<NetworkState> {
        return detailMediaDataSource.networkState
    }

}