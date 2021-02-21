package com.nasa.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.model.MediaPreview
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PreviewsMediaDataSource (private val apiService : NasaApiService, private val compositeDisposable: CompositeDisposable) {
    private val _networkState  = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedMediaPreviewsResponse =  MutableLiveData<List<MediaPreview>>()
    val downloadedMediaPreviewsResponse: LiveData<List<MediaPreview>>
        get() = _downloadedMediaPreviewsResponse

    fun fetchMediaPreviews(query:String){
        _networkState.postValue(NetworkState.LOADING)

        try{
            compositeDisposable.add(
                apiService.mediaPreview(query)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.e("MediaPreviewsDataSource", it.toString() )
                        _downloadedMediaPreviewsResponse.postValue(it.item)
                        _networkState.postValue(NetworkState.LOADED)
                    },{
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("MediaPreviewsDataSource", it.message.toString())
                    })
            )
        }
        catch (e: Exception){
            Log.e("MediaPreviewsDataSource", e.message.toString())
        }
    }
}