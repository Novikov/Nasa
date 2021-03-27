package com.nasa.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.model.media_detail.MediaDetail
import com.nasa.app.data.model.media_detail.MediaDetailResponse
import com.nasa.app.data.model.media_detail.raw_media_asset.RawMediaAssetsConverter
import com.nasa.app.data.model.media_detail.raw_media_detail.RawMediaDetailResponseConverter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class DetailMediaDataSource @Inject constructor(
    private val apiService: NasaApiService,
    private val compositeDisposable: CompositeDisposable,
    @Named("nasa id") private val nasaId: String
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedMediaDetailsResponse = MutableLiveData<MediaDetailResponse>()
    val downloadedMediaResponse: LiveData<MediaDetailResponse>
        get() = _downloadedMediaDetailsResponse

    @Inject lateinit var rawMediaDetailConverter: RawMediaDetailResponseConverter
    @Inject lateinit var rawMediaAssetConverter: RawMediaAssetsConverter

    fun fetchMediaDetails() {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.mediaInfo(nasaId)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        val rawMediaDetailResponse =
                            rawMediaDetailConverter.getMediaDetailResponseWithInfoData(it)
                        Log.i("MediaDetailsDataSource", rawMediaDetailResponse.item.toString())
                        apiService.mediaAsset(rawMediaDetailResponse.item.nasaId)
                            .observeOn(Schedulers.io())
                            .subscribeOn(Schedulers.io())
                            .map {
                                rawMediaDetailResponse.item.copy(
                                    assets = rawMediaAssetConverter.getAssets(it),
                                    metadataUrl = rawMediaAssetConverter.getMetadataUrl(it)
                                )
                            }
                    }
                    .flatMap { rawMediaDetailResponse ->
                        Log.i("MediaDetailsDataSource", rawMediaDetailResponse.toString())
                        apiService.mediaMetadata(rawMediaDetailResponse.metadataUrl!!)
                            .observeOn(Schedulers.io())
                            .subscribeOn(Schedulers.io())
                            .map {
                                rawMediaDetailResponse.copy(
                                    fileFormat = it.fileFormat,
                                    fileSize = it.fileSize
                                )
                            }
                    }
                    .subscribe({
                        Log.i("MediaDetailsDataSource", it.toString())
                        _downloadedMediaDetailsResponse.postValue(MediaDetailResponse(it))
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        if (it.message?.contains("Unable to resolve host")!!) {
                            _networkState.postValue(NetworkState.NO_INTERNET)
                        } else {
                            Log.i("MediaDetailsDataSource", it.message.toString())
                            _networkState.postValue(NetworkState.ERROR)
                        }
                    })
            )
        } catch (e: Exception) {
            Log.e("MediaDetailsDataSource", e.message.toString())
        }
    }


}