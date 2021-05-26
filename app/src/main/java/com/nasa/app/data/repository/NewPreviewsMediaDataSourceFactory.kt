package com.nasa.app.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.nasa.app.data.api.NasaApiService
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.ui.fragments.di.FragmentScope
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@FragmentScope
class NewPreviewsMediaDataSourceFactory @Inject constructor(
    private val previewLiveDataSource:NewPreviewsMediaDataSource
) : DataSource.Factory<Int, MediaPreview>() {

    val previewsLiveDataSource =  MutableLiveData<NewPreviewsMediaDataSource>()

    override fun create(): DataSource<Int, MediaPreview> {
        previewsLiveDataSource.postValue(previewLiveDataSource)
        return previewLiveDataSource
    }
}