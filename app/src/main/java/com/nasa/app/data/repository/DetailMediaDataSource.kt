package com.nasa.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.model.MediaDetail
import io.reactivex.Observable
import io.reactivex.Single
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
                fetchMediaAsset(it.item)
                },{
                    _networkState.postValue(NetworkState.ERROR)
                    Log.e("MediaDetailsDataSource", it.message.toString())
                })
            )
        }
        catch (e: Exception){
            Log.e("MediaDetailsDataSource", e.message.toString())
        }
    }

    fun fetchMediaAsset(mediaDetail: MediaDetail){
        try {
            compositeDisposable.add(
                apiService.mediaAsset(mediaDetail.nasaId)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        mediaDetail.assets = it.item
                        _downloadedMediaDetailsResponse.postValue(mediaDetail)
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        Log.e("MediaDetailsDataSource", it.message.toString())
                    })
            )
        }
        catch (e: Exception){
            Log.e("MediaDetailsDataSource", e.message.toString())
        }
    }
}