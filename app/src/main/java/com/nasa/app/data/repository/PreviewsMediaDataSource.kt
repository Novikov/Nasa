package com.nasa.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.model.media_preview.MediaPreviewResponse
import com.nasa.app.data.model.media_preview.raw_media_preview.RawMediaPreviewResponseConverter
import com.nasa.app.ui.fragment_media_preview.di.PreviewScope
import com.nasa.app.utils.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

@PreviewScope
class PreviewsMediaDataSource @Inject constructor(
    private val apiService: NasaApiService,
    private val compositeDisposable: CompositeDisposable,
    private val searchParams: SearchParams,
    private val rawMediaPreviewResponseConverter: RawMediaPreviewResponseConverter,
    @Named("initial media previews") private val _initialDownloadedMediaPreviewsResponse: MutableLiveData<MediaPreviewResponse>,
    @Named("media previews") private val _downloadedMediaPreviewsResponse: MutableLiveData<MediaPreviewResponse>,
    @Named("media previews network state") private val _networkState: MutableLiveData<NetworkState>
) {
    val networkState: LiveData<NetworkState>
        get() = _networkState

    val downloadedMediaPreviewsResponse: LiveData<MediaPreviewResponse>
        get() = _downloadedMediaPreviewsResponse

    val initialDownloadedMediaPreviewsResponse: LiveData<MediaPreviewResponse>
        get() = _initialDownloadedMediaPreviewsResponse

    fun getMediaPreviews() {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMediaPreviews(
                    searchParams.searchRequestQuery,
                    searchParams.getSearchMediaTypes(),
                    searchParams.startSearchYear,
                    searchParams.endSearchYear,
                    searchParams.searchPage
                )
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        val mediaPreviewResponse =
                            rawMediaPreviewResponseConverter.getMediaPreviewResponse(it)
                        _downloadedMediaPreviewsResponse.postValue(mediaPreviewResponse)

                        if(_initialDownloadedMediaPreviewsResponse.value==null){
                            _initialDownloadedMediaPreviewsResponse.postValue(mediaPreviewResponse)
                        }

                        if (mediaPreviewResponse.mediaPreviewList.isNotEmpty()) {
                            _networkState.postValue(NetworkState.LOADED)
                        } else {
                            _networkState.postValue(NetworkState.NOTHING_FOUND)
                        }
                    }, {
                        if (it.message?.contains(NO_INTERNET_ERROR_MSG_SUBSTRING)!!) {
                            _networkState.postValue(NetworkState.NO_INTERNET)
                        } else {
                            _networkState.postValue(NetworkState.ERROR)
                        }
                    })
            )
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    fun putInitialDataToDownloadedMediaResponse() {
        _downloadedMediaPreviewsResponse.postValue(_initialDownloadedMediaPreviewsResponse.value)
        _networkState.postValue(NetworkState.LOADED)
    }

    companion object {
        private const val TAG = "PreviewsMediaDataSource"
    }
}