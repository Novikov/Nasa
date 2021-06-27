package com.nasa.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.model.media_detail.MediaDetailResponse
import com.nasa.app.data.model.media_detail.raw_media_asset.RawMediaAssetsConverter
import com.nasa.app.data.model.media_detail.raw_media_detail.RawMediaDetailResponseConverter
import com.nasa.app.utils.HTTP_400_ERROR_MSG_SUBSTRING
import com.nasa.app.utils.HTTP_404_ERROR_MSG_SUBSTRING
import com.nasa.app.utils.NO_INTERNET_ERROR_MSG_SUBSTRING
import com.nasa.app.utils.TIMEOUT_ERROR_SUBSTRING
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class DetailMediaDataSource @Inject constructor(
    private val apiService: NasaApiService,
    @Named("Media Details Composite Disposable") private val compositeDisposable: CompositeDisposable,
    private val rawMediaDetailConverter: RawMediaDetailResponseConverter,
    private val rawMediaAssetConverter: RawMediaAssetsConverter,
    @Named("Media Details Network State") private val _networkState: MutableLiveData<NetworkState>
) {
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedMediaDetailsResponse = MutableLiveData<MediaDetailResponse>()
    val downloadedMediaResponse: LiveData<MediaDetailResponse>
        get() = _downloadedMediaDetailsResponse

    fun getMediaDetail(nasaId: String) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMediaDetailInfo(nasaId)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        val rawMediaDetailResponse =
                            rawMediaDetailConverter.getMediaDetailResponseWithInfoData(it)
                        Log.i(TAG, rawMediaDetailResponse.item.toString())
                        apiService.getMediaDetailAsset(rawMediaDetailResponse.item.nasaId)
                            .observeOn(Schedulers.io())
                            .subscribeOn(Schedulers.io())
                            .map {
                                rawMediaDetailResponse.item.copy(
                                    assets = rawMediaAssetConverter.getMediaDetailAssetResponse(it).assets,
                                    metadataUrl = rawMediaAssetConverter.getMetadataUrl(it)
                                )
                            }
                    }
                    .flatMap { rawMediaDetailResponse ->
                        Log.i(TAG, rawMediaDetailResponse.toString())
                        apiService.getMediaMetadata(rawMediaDetailResponse.metadataUrl!!)
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
                        Log.i(TAG, it.toString())
                        _downloadedMediaDetailsResponse.postValue(MediaDetailResponse(it))
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        when {
                            it.message?.contains(NO_INTERNET_ERROR_MSG_SUBSTRING)!! -> {
                                _networkState.postValue(NetworkState.NO_INTERNET)
                            }
                            it.message?.contains(TIMEOUT_ERROR_SUBSTRING)!! -> {
                                _networkState.postValue(NetworkState.TIMEOUT)
                            }
                            it.message?.contains(HTTP_400_ERROR_MSG_SUBSTRING)!! -> {
                                _networkState.postValue(NetworkState.BAD_REQUEST)
                            }
                            it.message?.contains(HTTP_404_ERROR_MSG_SUBSTRING)!! -> {
                                _networkState.postValue(NetworkState.NOT_FOUND)
                            }
                            else -> {
                                Log.i(TAG, it.message.toString())
                                _networkState.postValue(NetworkState.ERROR)
                            }
                        }
                    })
            )
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    companion object {
        private const val TAG = "DetailMediaDataSource"
    }
}