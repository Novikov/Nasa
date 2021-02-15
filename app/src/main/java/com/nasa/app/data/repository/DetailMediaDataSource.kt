package com.nasa.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.model.MediaDetail
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailMediaDataSource(private val apiService : NasaApiService, private val compositeDisposable: CompositeDisposable) {

    private val _networkState  = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedMediaDetailsResponse =  MutableLiveData<MediaDetail>()
    val downloadedMediaResponse: LiveData<MediaDetail>
        get() = _downloadedMediaDetailsResponse

    fun fetchMediaDetails(nasaId:String){
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
            apiService.mediaInfo(nasaId)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    Log.e("MediaDetail",it.toString())
                    _downloadedMediaDetailsResponse.postValue(it)
                    _networkState.postValue(NetworkState.LOADED)
                },{
                    _networkState.postValue(NetworkState.ERROR)
                    Log.e("MovieDetailsDataSource", it.message.toString())
                })
            )
        }
        catch (e: Exception){
            Log.e("MediaDetailsDataSource", e.message.toString())
        }
    }
}