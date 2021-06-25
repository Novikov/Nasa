package com.nasa.app.data.repository.initial_media_previews

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.data.model.media_preview.raw_media_preview.RawMediaPreviewResponseConverter
import com.nasa.app.data.repository.NetworkState
import com.nasa.app.ui.fragments.fragment_media_preview.initial.di.InitialMediaPreviewsCompositeDisposable
import com.nasa.app.ui.fragments.fragment_media_preview.initial.di.InitialMediaPreviewsNetworkState
import com.nasa.app.utils.FIRST_PAGE
import com.nasa.app.utils.NO_INTERNET_ERROR_MSG_SUBSTRING
import com.nasa.app.utils.SearchParams
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class InitialPreviewsMediaDataSource @Inject constructor(
    private val apiService: NasaApiService,
    @InitialMediaPreviewsCompositeDisposable private val compositeDisposable: CompositeDisposable,
    private val searchParams: SearchParams,
    private val rawMediaPreviewResponseConverter: RawMediaPreviewResponseConverter,
    @InitialMediaPreviewsNetworkState val networkState: MutableLiveData<NetworkState>
) : PageKeyedDataSource<Int, MediaPreview>() {

    private var page = FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MediaPreview>
    ) {
        networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMediaPreviews(
                    searchParams.getDefaultSearchQuery(),
                    searchParams.getDefaultSearchMediaTypes(),
                    searchParams.startSearchYear,
                    searchParams.endSearchYear,
                    page
                )
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        if (it.collection.items.size>0){
                            val mediaPreviewResponse =
                                rawMediaPreviewResponseConverter.getMediaPreviewResponse(it)
                            callback.onResult(mediaPreviewResponse.mediaPreviewList, null, page + 1)
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else {
                            networkState.postValue(NetworkState.NOTHING_FOUND)
                        }
                    }, {
                        if (it.message?.contains(NO_INTERNET_ERROR_MSG_SUBSTRING)!!) {
                            networkState.postValue(NetworkState.NO_INTERNET)
                        } else {
                            networkState.postValue(NetworkState.ERROR)
                        }
                    })
            )
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MediaPreview>) {
        networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMediaPreviews(
                    searchParams.getDefaultSearchQuery(),
                    searchParams.getDefaultSearchMediaTypes(),
                    searchParams.beginDate,
                    searchParams.endDate,
                    params.key
                )
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        val mediaPreviewResponse = rawMediaPreviewResponseConverter.getMediaPreviewResponse(it)

                        if(mediaPreviewResponse.totalPages >= params.key) {
                            callback.onResult(mediaPreviewResponse.mediaPreviewList, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    }, {
                        if (it.message?.contains(NO_INTERNET_ERROR_MSG_SUBSTRING)!!) {
                            networkState.postValue(NetworkState.NO_INTERNET)
                        } else {
                            networkState.postValue(NetworkState.ERROR)
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
        private const val TAG = "PreviewsMediaDataSource"
    }
}
