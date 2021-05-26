package com.nasa.app.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.model.media_preview.MediaPreviewResponse
import com.nasa.app.data.model.media_preview.raw_media_preview.RawMediaPreviewResponseConverter
import com.nasa.app.ui.fragments.di.FragmentScope
import com.nasa.app.utils.FIRST_PAGE
import com.nasa.app.utils.NO_INTERNET_ERROR_MSG_SUBSTRING
import com.nasa.app.utils.SearchParams
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

@FragmentScope
class NewPreviewsMediaDataSource @Inject constructor(
    private val apiService: NasaApiService,
    private val compositeDisposable: CompositeDisposable,
    private val searchParams: SearchParams,
    private val rawMediaPreviewResponseConverter: RawMediaPreviewResponseConverter,
//    @Named("media previews") private val _downloadedMediaPreviewsResponse: MutableLiveData<MediaPreviewResponse>,
    @Named("media previews network state") private val _networkState: MutableLiveData<NetworkState>
): PageKeyedDataSource<Int, MediaPreview>() {

    private var page = FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MediaPreview>
    ) {
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
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        val mediaPreviewResponse =
                            rawMediaPreviewResponseConverter.getMediaPreviewResponse(it)
                        callback.onResult(mediaPreviewResponse.mediaPreviewList,null,page+1)

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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MediaPreview>) {
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
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        val mediaPreviewResponse =
                            rawMediaPreviewResponseConverter.getMediaPreviewResponse(it)
                        callback.onResult(mediaPreviewResponse.mediaPreviewList,params.key+1)

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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MediaPreview>) {
        //nothing happened
    }

    companion object {
        private const val TAG = "NewPreviewsMediaDataSou"
    }
}
