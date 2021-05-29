package com.nasa.app.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.ui.fragments.di.FragmentScope
import javax.inject.Inject

@FragmentScope
class PreviewsMediaDataSourceFactory @Inject constructor(
    private val previewLiveDataSource:PreviewsMediaDataSource
) : DataSource.Factory<Int, MediaPreview>() {

    val previewsLiveDataSource =  MutableLiveData<PreviewsMediaDataSource>()

    override fun create(): DataSource<Int, MediaPreview> {
        previewsLiveDataSource.postValue(previewLiveDataSource)
        return previewLiveDataSource
    }
}