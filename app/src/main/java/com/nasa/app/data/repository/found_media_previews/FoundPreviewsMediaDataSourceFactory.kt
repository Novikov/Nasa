package com.nasa.app.data.repository.found_media_previews

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.nasa.app.data.model.media_preview.MediaPreview
import com.nasa.app.ui.fragments.di.FragmentScope
import javax.inject.Inject

@FragmentScope
class FoundPreviewsMediaDataSourceFactory @Inject constructor(
    private val previewLiveDataSourceInitial: FoundPreviewsMediaDataSource
) : DataSource.Factory<Int, MediaPreview>() {

    val previewsLiveDataSource =  MutableLiveData<FoundPreviewsMediaDataSource>()

    override fun create(): DataSource<Int, MediaPreview> {
        previewsLiveDataSource.postValue(previewLiveDataSourceInitial)
        return previewLiveDataSourceInitial
    }
}