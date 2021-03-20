package com.nasa.app.ui.media_detail

import androidx.lifecycle.LiveData
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.model.MediaDetail
import com.nasa.app.data.repository.DetailMediaDataSource
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.media_detail.di.DetailScope
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@DetailScope
class DetailMediaRepository @Inject constructor(private val apiService: NasaApiService) {
   @Inject lateinit var detailMediaDataSource: DetailMediaDataSource

    fun fetchSingleMediaDetail(
        compositeDisposable: CompositeDisposable,
        nasaId: String
    ): LiveData<MediaDetail> {
//        detailMediaDataSource = DetailMediaDataSource(apiService, compositeDisposable)
        detailMediaDataSource.fetchMediaDetails(nasaId)
        return detailMediaDataSource.downloadedMediaResponse
    }

    fun getMediaDetailsNetworkState(): LiveData<NetworkState> {
        return detailMediaDataSource.networkState
    }

}