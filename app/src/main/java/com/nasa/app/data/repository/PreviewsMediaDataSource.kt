package com.nasa.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.api.json.MediaPreviewResponse
import com.nasa.app.data.model.MediaPreview
import com.nasa.app.ui.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PreviewsMediaDataSource @Inject constructor(private val apiService: NasaApiService, private val compositeDisposable: CompositeDisposable) {
    @Inject lateinit var searchParams: SearchParams

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedMediaPreviewsResponse = MutableLiveData<MediaPreviewResponse>()
    val downloadedMediaPreviewsResponse: LiveData<MediaPreviewResponse>
        get() = _downloadedMediaPreviewsResponse

    fun fetchMediaPreviews() {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.mediaPreview(
                    searchParams.SEARCH_REQUEST_QUERY,
                    searchParams.getSearchMediaTypes(),
                    searchParams.SEARCH_YEAR_START,
                    searchParams.SEARCH_YEAR_END,
                    searchParams.SEARCH_PAGE
                )
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.i("MediaPreviewsDataSource", it.mediaPreviewList.size.toString())
                        _downloadedMediaPreviewsResponse.postValue(it)
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
            Log.e("MediaPreviewsDataSource", e.message.toString())
        }
    }
}