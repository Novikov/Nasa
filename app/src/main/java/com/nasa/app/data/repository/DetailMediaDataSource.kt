package com.nasa.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.model.media_detail.MediaDetail
import com.nasa.app.data.model.media_detail.raw_media_detail.RawMediaDetailResponseConverter
import com.nasa.app.data.model.media_preview.raw_data.RawMediaPreviewResponseConverter
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

    private val _downloadedMediaDetailsResponse = MutableLiveData<MediaDetail>()
    val downloadedMediaResponse: LiveData<MediaDetail>
        get() = _downloadedMediaDetailsResponse

    @Inject lateinit var rawMediaDetailConverter: RawMediaDetailResponseConverter

    fun fetchMediaDetails() {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.mediaInfo(nasaId)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .flatMap { rawMediaResponse ->
                        apiService.mediaAsset(rawMediaResponse.item.nasaId)
                            .observeOn(Schedulers.io())
                            .subscribeOn(Schedulers.io())
                            .map {
                                rawMediaResponse.item.copy(
                                    assets = it.assetMap,
                                    metadataUrl = it.metadataUrl
                                )
                            }
                    }
                    .flatMap { rawMediaResponse ->
                        apiService.mediaMetadata(rawMediaResponse.metadataUrl!!)
                            .observeOn(Schedulers.io())
                            .subscribeOn(Schedulers.io())
                            .map {
                                rawMediaResponse.copy(
                                    fileFormat = it.fileFormat,
                                    fileSize = it.fileSize
                                )
                            }
                    }
                    .subscribe({
                        Log.i("TAG", it.toString())
                        _downloadedMediaDetailsResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        if (it.message?.contains("Unable to resolve host")!!) {
                            _networkState.postValue(NetworkState.NO_INTERNET)
                        } else {
                            _networkState.postValue(NetworkState.ERROR)
                        }
                    })
            )
        } catch (e: Exception) {
            Log.e("MediaDetailsDataSource", e.message.toString())
        }
    }

    fun fetchMediaDetails2() {
//        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.mediaInfo2(nasaId)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
//                    .flatMap { rawMediaResponse ->
//                        apiService.mediaAsset(rawMediaResponse.item.nasaId)
//                            .observeOn(Schedulers.io())
//                            .subscribeOn(Schedulers.io())
//                            .map {
//                                rawMediaResponse.item.copy(
//                                    assets = it.assetMap,
//                                    metadataUrl = it.metadataUrl
//                                )
//                            }
//                    }
                    .subscribe({
                        val mediaDetailResponse = rawMediaDetailConverter.getMediaDetailResponseWithInfoData(it)
                        Log.i("ZZZ", mediaDetailResponse.item.toString())
//                        _downloadedMediaDetailsResponse.postValue(it)
//                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        if (it.message?.contains("Unable to resolve host")!!) {
//                            _networkState.postValue(NetworkState.NO_INTERNET)
                        } else {
//                            _networkState.postValue(NetworkState.ERROR)
                        }
                    })
            )
        } catch (e: Exception) {
            Log.e("MediaDetailsDataSource", e.message.toString())
        }
    }


}