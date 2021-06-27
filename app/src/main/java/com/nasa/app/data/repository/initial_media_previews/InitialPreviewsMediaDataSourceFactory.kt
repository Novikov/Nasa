package com.nasa.app.data.repository.initial_media_previews

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.nasa.app.data.model.media_preview.MediaPreview
import javax.inject.Inject

class InitialPreviewsMediaDataSourceFactory @Inject constructor(
    private val previewLiveDataSourceInitial: InitialPreviewsMediaDataSource
) : DataSource.Factory<Int, MediaPreview>() {

    val previewsLiveDataSource =  MutableLiveData<InitialPreviewsMediaDataSource>()

    override fun create(): DataSource<Int, MediaPreview> {
        previewsLiveDataSource.postValue(previewLiveDataSourceInitial)
        return previewLiveDataSourceInitial
    }
}